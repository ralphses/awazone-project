package net.awazone.awazoneproject.service.servicesImpl.aibopay;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.ResourceNotFoundException;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.aibopay.UserWallet;
import net.awazone.awazoneproject.model.aibopay.transaction.AibopayTransaction;
import net.awazone.awazoneproject.model.aibopay.transaction.TransactionStatus;
import net.awazone.awazoneproject.model.aibopay.transaction.TransactionType;
import net.awazone.awazoneproject.model.response.AccountTransactionResponse;
import net.awazone.awazoneproject.repository.aibopay.TransactionRepository;
import net.awazone.awazoneproject.repository.aibopay.UserWalletRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.TransactionService;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.PaymentProcessor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserWalletRepository userWalletRepository;
    private final PaymentProcessor paymentProcessor;

    @Override
    public AibopayTransaction saveNewTransaction(TransactionType transactionType,
                                                 String reference,
                                                 String beneficiary,
                                                 String description,
                                                 BigDecimal amount,
                                                 BigDecimal fees,
                                                 String walletId,
                                                 TransactionStatus transactionStatus,
                                                 String paymentMethod) {

        UserWallet userWallet = userWalletRepository.findByAddress(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid wallet address " + walletId));

        AibopayTransaction aibopayTransaction = AibopayTransaction.builder()
                .amount(amount)
                .userWallet(userWallet)
                .beneficiary(beneficiary)
                .description(description)
                .transactionType(transactionType)
                .transactionId(reference)
                .createdAt(LocalDateTime.now())
                .transactionFees(fees)
                .transactionStatus(transactionStatus)
                .paymentMethod(paymentMethod)
                .build();
        return saveTransaction(aibopayTransaction);
    }

    @Override
    public AibopayTransaction saveTransaction(AibopayTransaction aibopayTransaction) {
        return transactionRepository.save(aibopayTransaction);
    }


    @Override
    public void updateTransactionStatus(String transactionId, TransactionStatus transactionStatus) {
        findByTransactionId(transactionId).setTransactionStatus(transactionStatus);
    }

    @Override
    public AibopayTransaction findByTransactionId(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with id " + transactionId + " not found"));
    }

    @Override
    public List<AibopayTransaction> findAllByWallet(UserWallet userWallet, int page) {
        return transactionRepository.findAllByUserWallet(userWallet, PageRequest.of(page-1, 10)).getContent();
    }

    @Override
    public ResponseMessage findAll(int page) {
        List<AibopayTransaction> transactions = transactionRepository
                .findAll(PageRequest.of(page-1, 10)).getContent();
        return new ResponseMessage("success", OK, Map.of("transactions", transactions));
    }

    @Override
    public ResponseMessage getTransactionsForAccount(String accountReference, int page, int numberOfRecord) {
        AccountTransactionResponse accountTransactionResponse =
                (AccountTransactionResponse) paymentProcessor.getTransactionsForAccount(accountReference, page, numberOfRecord);
        return new ResponseMessage("success", OK, Map.of("content", accountTransactionResponse));
    }


}
