package net.awazone.awazoneproject.model.aibopay.transaction.virtualTransactions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaction {
    private CustomerDTO customerDTO;
    private String providerAmount;
    private String paymentMethod;
    private String createdOn;
    private String amount;
    private String flagged;
    private String providerCode;
    private String fee;
    private String currencyCode;
    private String completedOn;
    private String paymentDescription;
    private String paymentStatus;
    private String transactionReference;
    private String paymentReference;
    private String merchantCode;
    private String merchantName;
    private String payableAmount;
    private String amountPaid;
    private String completed;
}
