package net.awazone.awazoneproject.service.servicesImpl.aibopay;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.configuration.coinremmita.CoinRemitterCredential;
import net.awazone.awazoneproject.exception.CustomInvalidParamException;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.exception.VirtualAccountException;
import net.awazone.awazoneproject.model.aibopay.UserWallet;
import net.awazone.awazoneproject.model.aibopay.account.AibopayAccount;
import net.awazone.awazoneproject.model.aibopay.transaction.AibopayTransaction;
import net.awazone.awazoneproject.model.aibopay.transaction.TransactionStatus;
import net.awazone.awazoneproject.model.aibopay.transaction.TransactionType;
import net.awazone.awazoneproject.model.dtos.aibopay.NewPayWithBankTransferRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.TransactionRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewCardPaymentRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.coinremmitter.NewCryptoPaymentRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewInitPaymentRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.webhook.AccountTransactionNotificationRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.webhook.EventData;
import net.awazone.awazoneproject.model.response.CardPaymentResponse;
import net.awazone.awazoneproject.model.response.NewPaymentInitResponse;
import net.awazone.awazoneproject.model.response.TransactionStatusResponse;
import net.awazone.awazoneproject.repository.aibopay.TransactionRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.TransactionService;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.AccountService;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.PaymentProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.math.BigDecimal.valueOf;
import static net.awazone.awazoneproject.model.aibopay.transaction.TransactionStatus.COMPLETED;
import static net.awazone.awazoneproject.model.aibopay.transaction.TransactionStatus.PAID;
import static net.awazone.awazoneproject.model.aibopay.transaction.TransactionType.*;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@AllArgsConstructor
public class PaymentService {

    private final TransactionService transactionService;
    private final PaymentProcessor paymentProcessor;
    private final CoinRemmitaPaymentProcessor coinRemmitaPaymentProcessor;
    private final AccountService accountService;
    private final WalletServiceImpl walletService;
    private final TransactionRepository transactionRepository;

    public void saveTransaction(NewInitPaymentRequest newPaymentInitRequest, ResponseMessage responseMessage) {
        NewPaymentInitResponse response = (NewPaymentInitResponse) responseMessage.getResponseBody().get("response");

        String enabledPaymentMethod = String.join(" | ", response.getEnabledPaymentMethod());
        transactionService.saveNewTransaction(
                DEPOSIT,
                response.getTransactionReference(),
                response.getMerchantName(),
                newPaymentInitRequest.getPaymentDescription(),
                valueOf(newPaymentInitRequest.getAmount()),
                valueOf(newPaymentInitRequest.getAmount() * 0.01),
                "AWAZONE",
                TransactionStatus.PENDING,
                enabledPaymentMethod
        );
    }

    public ResponseMessage initPayment(NewInitPaymentRequest newPaymentInitRequest) {
        return paymentProcessor.initPayment(newPaymentInitRequest);
    }

    public ResponseMessage acceptCardPayment(NewCardPaymentRequest newCardPaymentRequest) {

        ResponseMessage responseMessage = paymentProcessor.acceptCardPayment(newCardPaymentRequest);
        CardPaymentResponse paymentData = (CardPaymentResponse) responseMessage.getResponseBody().get("paymentData");

        boolean isSuccessful = Objects.equals(paymentData.getStatus(), "SUCCESS");

        if(isSuccessful) {
            transactionRepository
                    .findByTransactionId(paymentData.getTransactionReference())
                    .ifPresent(aibopayTransaction -> aibopayTransaction.setTransactionStatus(PAID));
        }
        return responseMessage;
    }

    @SuppressWarnings("unchecked")
    public ResponseMessage acceptCryptoPayment(String coinType, String amount) {

        try {
            double realAmount = Double.parseDouble(amount);

            NewCryptoPaymentRequest newCryptoPaymentRequest = NewCryptoPaymentRequest.builder()
                    .api_key(CoinRemitterCredential.TCN_API_KEY)
                    .password(CoinRemitterCredential.PASSWORD)
                    .amount(realAmount)
                    .build();

            Map<String, Object> response = coinRemmitaPaymentProcessor.processPayment(coinType, newCryptoPaymentRequest);

            HashMap<String, String> total_amount1 = new ObjectMapper().convertValue(response.get("total_amount"), HashMap.class);

            String key = total_amount1.keySet().stream()
                    .filter(ke -> ke.equals(coinType))
                    .collect(Collectors.joining());

            saveNewTransaction(
                    DEPOSIT,
                    (String) response.get("description"),
                    (String)response.get("invoice_id"),
                    Double.parseDouble(total_amount1.get(key)),
                    "name",
                    "AWAZONE",
                    TransactionStatus.valueOf(((String) response.get("status")).toUpperCase()),
                    "CRYPTO"
            );
            return new ResponseMessage("success", OK, response);

        }catch (NumberFormatException e) {
            throw new CustomInvalidParamException("Invalid amount entered");
        }
    }

    public ResponseMessage fullPaymentStatus(String paymentReference) {
        return paymentProcessor.paymentStatus(paymentReference);
    }

    public ResponseMessage paymentStatus(String paymentReference, String coinType) {

        if(coinType != null) {
            return new ResponseMessage(
                    "Success",
                    OK,
                    Map.of("status", coinRemmitaPaymentProcessor.checkInvoiceStatus(paymentReference, coinType)));
        }
        else {
            String status = ((TransactionStatusResponse) paymentProcessor.paymentStatus(paymentReference)
                    .getResponseBody().get("status")).getPaymentStatus();
            return new ResponseMessage("success", OK, Map.of("status", status));
        }

    }

    public ResponseMessage getPayment(String paymentReference) {
        return paymentProcessor.paymentStatus(paymentReference);
    }

    public ResponseMessage updateTransaction(
            AccountTransactionNotificationRequest accountTransactionNotificationRequest) {

        EventData eventData = accountTransactionNotificationRequest.getEventData();

        String transactionReference = eventData.getTransactionReference();
        String incomingPaymentStatus = eventData.getPaymentStatus();
        String accountNumber =  eventData.getDestinationAccountInformation().getAccountNumber();
        String paymentDescription = eventData.getPaymentDescription();
        double amount = eventData.getAmountPaid();

        AibopayAccount aibopayAccount = accountService.getAccount(accountNumber);
        UserWallet userWallet = walletService.getWalletByAccountNumber(accountNumber);
        TransactionStatus transactionStatus = TransactionStatus.valueOf(incomingPaymentStatus);

        //Confirm transaction status from payment processor
        TransactionStatusResponse transactionStatusResponse =
                (TransactionStatusResponse) fullPaymentStatus(transactionReference).getResponseBody().get("status");

        //Validate incoming transaction against real transaction
        boolean validRequest = Objects.equals(transactionReference, transactionStatusResponse.getTransactionReference()) &&
                Objects.equals(incomingPaymentStatus, transactionStatusResponse.getPaymentStatus()) &&
                Double.compare(amount, Double.parseDouble(transactionStatusResponse.getAmountPaid())) == 0;

        if(!validRequest) {
            throw new VirtualAccountException("Unknown Transaction " + transactionReference);
        }

        String paymentMethod = eventData.getPaymentMethod();

        //Check for successful transaction only
        if(Objects.equals(accountTransactionNotificationRequest.getEventType(), "SUCCESSFUL_TRANSACTION") &&
                incomingPaymentStatus.equals("PAID")) {

            Optional<AibopayTransaction> optionalAibopayTransaction =
                    transactionRepository.findByTransactionId(transactionReference);

            if(optionalAibopayTransaction.isPresent()) {
                AibopayTransaction aibopayTransaction = optionalAibopayTransaction.get();

                //Check if this transaction is already completed
                if(aibopayTransaction.getTransactionStatus().equals(PAID)) {
                    throw new VirtualAccountException("Transaction already confirmed for transaction " + transactionReference);
                }

                aibopayTransaction.setTransactionStatus(PAID);
                aibopayAccount.topFund(amount);
            }
            else {
                saveNewTransaction(TRANSFER,
                        paymentDescription,
                        transactionReference,
                        amount,
                        aibopayAccount.getCustomerName(),
                        userWallet.getAddress(),
                        PAID,
                        paymentMethod);

                aibopayAccount.topFund(amount);
            }
        }
        else if (Objects.equals(accountTransactionNotificationRequest.getEventType(), "SUCCESSFUL_TRANSACTION")){

                saveNewTransaction(TRANSFER,
                        paymentDescription,
                        transactionReference,
                        amount,
                        aibopayAccount.getCustomerName(),
                        userWallet.getAddress(),
                        transactionStatus,
                        paymentMethod);
        }
        return new ResponseMessage("verified", OK, Map.of("verified", true));
    }

    public ResponseMessage acceptTransferPayment(NewPayWithBankTransferRequest newPayWithBankTransferRequest) {
        return paymentProcessor.acceptTransferPayment(newPayWithBankTransferRequest);
    }

    public ResponseMessage getAllBanks() {
        return new ResponseMessage("success", OK, Map.of("banks", paymentProcessor.getAllBanksAndUSSDCodes()));
    }

    private void saveNewTransaction(TransactionType transactionType,
                                    String paymentDescription,
                                    String transactionReference,
                                    double amount,
                                    String customerName,
                                    String address,
                                    TransactionStatus status,
                                    String paymentMethod) {
        transactionService.saveNewTransaction(
                transactionType,
                transactionReference,
                customerName,
                paymentDescription,
                valueOf(amount),
                valueOf(10.0),
                address,
                status,
                paymentMethod);
    }

    public ResponseMessage performTransaction(TransactionRequest transactionRequest) {

        String username = transactionRequest.getUsername();
        String description = transactionRequest.getDescription();
        String transactionType = transactionRequest.getTransactionType();

        double amount = transactionRequest.getAmount();

        UserWallet userWallet = walletService.findByUsername(username);
        AibopayAccount account = userWallet.getAccount();
        LocalDateTime dateCreated = LocalDateTime.now();

        if(transactionType.equalsIgnoreCase("WITHDRAWAL") &&
                Double.compare(account.getCurrentBalance().doubleValue(), amount) == 1) {

            account.lessFund(amount);
        }
        else if(transactionType.equalsIgnoreCase("DEPOSIT")) {
            account.topFund(amount);
        }
        else {
            throw new CustomInvalidParamException("Invalid transaction type " + transactionType);
        }


        AibopayTransaction aibopayTransaction = AibopayTransaction.builder()
                .paymentMethod(transactionType)
                .description(description)
                .transactionType(AIBO_EARN)
                .createdAt(dateCreated)
                .transactionId(UUID.nameUUIDFromBytes((transactionType + description + dateCreated + username).getBytes()).toString())
                .beneficiary("AWAZONE")
                .transactionFees(BigDecimal.valueOf(0.0))
                .transactionStatus(COMPLETED)
                .amount(BigDecimal.valueOf(amount))
                .userWallet(userWallet)
                .build();

        return new ResponseMessage("success", OK, Map.of("transaction", transactionRepository.save(aibopayTransaction)));
    }

}
