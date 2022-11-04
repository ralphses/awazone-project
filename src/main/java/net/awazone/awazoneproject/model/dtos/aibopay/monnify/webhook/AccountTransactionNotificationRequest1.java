package net.awazone.awazoneproject.model.dtos.aibopay.monnify.webhook;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountTransactionNotificationRequest1 {

    private String eventType;
    private Object eventData;
    private String transactionReference;
    private String paymentReference;
    private String paidOn;
    private String paymentDescription;
    private Object metaData;
    private AccountDetails[] accountDetails;
    private Object destinationAccountInformation;
    private String amountPaid;
    private String totalPayable;
    private Object cardDetails;
    private String paymentMethod;
    private String currency;
    private String settlementAmount;
    private String paymentStatus;
    private Object customer;

}
