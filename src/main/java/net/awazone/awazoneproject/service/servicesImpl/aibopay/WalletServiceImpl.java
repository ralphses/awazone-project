package net.awazone.awazoneproject.service.servicesImpl.aibopay;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.InsufficientResourceException;
import net.awazone.awazoneproject.exception.ResourceNotFoundException;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.aibopay.UserWallet;
import net.awazone.awazoneproject.model.aibopay.account.AibopayAccount;
import net.awazone.awazoneproject.model.aibopay.transaction.AibopayTransaction;
import net.awazone.awazoneproject.model.aibopay.transaction.TransactionStatus;
import net.awazone.awazoneproject.model.aibopay.transaction.virtualTransactions.Transaction;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewTransferRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.ViewAccountStatementRequest;
import net.awazone.awazoneproject.model.response.AccountTransactionResponse;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.repository.aibopay.UserWalletRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.TransactionService;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.AccountService;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.PaymentProcessor;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.WalletService;
import net.awazone.awazoneproject.service.serviceInterfaces.user.AwazoneUserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.*;
import static net.awazone.awazoneproject.model.aibopay.transaction.TransactionStatus.COMPLETED;
import static net.awazone.awazoneproject.model.aibopay.transaction.TransactionType.DEPOSIT;
import static net.awazone.awazoneproject.model.aibopay.transaction.TransactionType.TRANSFER;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final UserWalletRepository userWalletRepository;
    private final AwazoneUserService awazoneUserService;
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final PaymentProcessor paymentProcessor;

    @Override
    public ResponseMessage loadWallet(String username) {

        //Find user with username
        AwazoneUser awazoneUser = awazoneUserService.findAppUserByUsername(username);

        //Find user wallet
        UserWallet userWallet = getUserWallet(awazoneUser);

        List<AibopayTransaction> transactions = transactionService.findAllByWallet(userWallet, 1);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("wallet", userWallet);
        responseBody.put("transactions", transactions);

        return ResponseMessage.builder()
                .httpStatus(OK)
                .message("Success")
                .responseBody(responseBody)
                .build();
    }

    @Override
    public UserWallet findByUsername(String username) {
        AwazoneUser user = awazoneUserService.findAppUserByEmail(username);
        return getUserWallet(user);
    }

    @Override
    public ResponseMessage transferFund(NewTransferRequest newTransferRequest, Authentication authentication) {

        //Confirm he/she has enough balance and is authorized
        double amount =  Double.parseDouble(newTransferRequest.getAmount());
        double transferFee = calculateTransferFee(amount);

        String principal = (String) authentication.getPrincipal();
        UserWallet senderWallet = getUserWallet(awazoneUserService.findAppUserByUsername(principal));

        AibopayAccount senderWalletAccount = senderWallet.getAccount();
        double currentBalance = senderWalletAccount.getCurrentBalance().doubleValue();

        if(Double.compare(currentBalance, (amount + transferFee)) == -1) {
            throw new InsufficientResourceException("Insufficient amount, current balance: " + currentBalance);
        }

        //Generate Reference ID
        String transactionReference = UUID.randomUUID().toString();

        AibopayTransaction aibopayTransaction;

        if(newTransferRequest.getDestinationBankCode().equalsIgnoreCase("000")) {
            //Transfer to local account
            aibopayTransaction = transferToAiboAccount(
                    Double.parseDouble(newTransferRequest.getAmount()),
                    newTransferRequest.getDestinationAccountNumber(),
                    transactionReference);
        }
        else {
            //Transfer to other account
            paymentProcessor.initiateSingleTransfer(newTransferRequest, transactionReference);

            aibopayTransaction = transactionService.saveNewTransaction(
                    TRANSFER,
                    transactionReference,
                    newTransferRequest.getDestinationAccountNumber(),
                    newTransferRequest.getNarration(),
                    valueOf(amount),
                    valueOf(transferFee),
                    senderWallet.getAddress(),
                    COMPLETED,
                    "EXTERNAL TRANSFER");
        }

        //Update this user account
        senderWalletAccount.lessFund(amount + transferFee);

        return new ResponseMessage("success", OK, Map.of("responseBody", aibopayTransaction));
    }

    private AibopayTransaction transferToAiboAccount(double amount, String destinationAccountNumber, String reference) {

        AibopayAccount recipientAccount = (AibopayAccount) accountService
                .findAccountByAccountNumber(destinationAccountNumber).getResponseBody().get("account");

        UserWallet recipientWallet = getWalletByAccountNumber(destinationAccountNumber);

        AibopayTransaction aibopayTransaction = transactionService.saveNewTransaction(
                DEPOSIT,
                reference,
                recipientWallet.getAwazoneUser().getFullName(),
                DEPOSIT.name(),
                valueOf(amount),
                valueOf(0.0),
                recipientWallet.getAddress(),
                COMPLETED,
                "INTERNAL TRANSFER");

        recipientAccount.topFund(amount);

        return aibopayTransaction;
    }

    public UserWallet getWalletByAccountNumber(String destinationAccountNumber) {
        return userWalletRepository.findByAccountAccountNumber(destinationAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("No wallet created for this account number " + destinationAccountNumber));
    }

    @Override
    public ResponseMessage viewAccountStatement(ViewAccountStatementRequest viewAccountStatementRequest,
                                                LocalDate startDate,
                                                LocalDate endDate,
                                                Integer page,
                                                Authentication authentication) {
        //Todo: authenticate user
        String principal = (String) authentication.getPrincipal();
        UserWallet userWallet = getUserWallet(awazoneUserService.findAppUserByUsername(principal));

        //Todo: Check if start and end date is provided
        if(startDate == null || endDate == null) {
            List<AibopayTransaction> localTransactions = transactionService.findAllByWallet(userWallet, page);
            List<AibopayTransaction> virtualTransactions = findAllVirtualTransactions(userWallet);

            localTransactions.addAll(virtualTransactions);
            List<AibopayTransaction> sortedTransactions = sortTransaction(localTransactions);

            return new ResponseMessage("success", OK, Map.of("transactions", sortedTransactions));
        }
        else {
            List<AibopayTransaction> localTransactions =
                    findBoundedTransaction(transactionService.findAllByWallet(userWallet, page), startDate, endDate);
            List<AibopayTransaction> virtualTransactions =
                    findBoundedTransaction(findAllVirtualTransactions(userWallet), startDate, endDate);

            localTransactions.addAll(virtualTransactions);
            List<AibopayTransaction> sortedTransactions = sortTransaction(localTransactions);

            return new ResponseMessage("success", OK, Map.of("transactions", sortedTransactions));
        }
    }

    @Override
    public UserWallet createWallet(AwazoneUser awazoneUser, AibopayAccount aibopayAccount) {
        return userWalletRepository.save(
                UserWallet.builder()
                .account(aibopayAccount)
                .address(UUID.fromString(awazoneUser.getFullName()).toString())
                .build());
    }

    private List<AibopayTransaction> sortTransaction(List<AibopayTransaction> localTransactions) {
        return localTransactions.stream().sorted((t1, t2) -> {
            if (t1.getCreatedAt().isBefore(t2.getCreatedAt())) return 0;
            else return -1;
        }).toList();
    }

    private List<AibopayTransaction> findBoundedTransaction(List<AibopayTransaction> transactions,
                                                            LocalDate startDate,
                                                            LocalDate endDate) {
        return transactions.stream()
                .filter(t -> t.getCreatedAt()
                        .isAfter(LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 0, 0, 0, 0)) &&
                        t.getCreatedAt().isBefore(LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 0, 0, 0))).toList();
    }

    private List<AibopayTransaction> findAllVirtualTransactions(UserWallet userWallet) {
        List<AibopayTransaction> virtualTransactions = new ArrayList<>();
        List<Transaction> transactions = getVirtualTransaction(userWallet.getAccount().getAccountReference());

        transactions
                .forEach(trans -> virtualTransactions.add(AibopayTransaction.builder()
                        .transactionStatus(TransactionStatus.valueOf(trans.getPaymentStatus()))
                        .transactionFees(BigDecimal.valueOf(0.0))
                        .transactionId(trans.getTransactionReference())
                        .userWallet(userWallet)
                        .amount(BigDecimal.valueOf(Double.parseDouble(trans.getAmount())))
                        .beneficiary(trans.getCustomerDTO().getName())
                        .createdAt(LocalDateTime.parse(trans.getCreatedOn().replace("+", "")))
                        .transactionType(DEPOSIT)
                        .description(trans.getPaymentDescription())
                        .paymentMethod("ACCOUNT_TRANSFER")
                        .build()));

        return virtualTransactions;
    }

    private Double calculateTransferFee(double amount) {
        return amount * 0.01;
    }



    private List<Transaction> getVirtualTransaction(String accountReference) {
        AccountTransactionResponse accountTransactionResponse = (AccountTransactionResponse) paymentProcessor.getTransactionsForAccount(accountReference);
        return stream(accountTransactionResponse.getContent()).toList();
    }

    private UserWallet getUserWallet(AwazoneUser awazoneUser) {
        Optional<UserWallet> userWallet = userWalletRepository.findByAwazoneUser(awazoneUser);

        if(userWallet.isPresent()) {
            return userWallet.get();
        }
        else {
            AibopayAccount aibopayAccount =
                    accountService.findAccountByReference(awazoneUser.getAwazoneUserDomain().getDomainName());

            return createWallet(awazoneUser, aibopayAccount);
        }
    }

}
