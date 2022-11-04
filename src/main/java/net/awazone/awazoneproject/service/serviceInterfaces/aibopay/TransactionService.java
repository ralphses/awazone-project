package net.awazone.awazoneproject.service.serviceInterfaces.aibopay;

import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.aibopay.UserWallet;
import net.awazone.awazoneproject.model.aibopay.transaction.AibopayTransaction;
import net.awazone.awazoneproject.model.aibopay.transaction.TransactionStatus;
import net.awazone.awazoneproject.model.aibopay.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    AibopayTransaction saveNewTransaction(TransactionType transactionType, String reference, String beneficiary, String description, BigDecimal amount, BigDecimal TransactionFees, String walletId,  TransactionStatus transactionStatus, String paymentMethod);
    void updateTransactionStatus(String transactionId, TransactionStatus transactionStatus);

    AibopayTransaction saveTransaction(AibopayTransaction aibopayTransaction);

    AibopayTransaction findByTransactionId(String transactionId);
    List<AibopayTransaction> findAllByWallet(UserWallet userWallet, int page);

    ResponseMessage findAll(int page);

    ResponseMessage getTransactionsForAccount(String accountReference, int page, int numberOfRecord);
}
