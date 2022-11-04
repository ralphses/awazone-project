package net.awazone.awazoneproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.webhook.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionStatusResponse {

    private String transactionReference;
    private String paymentReference;
    private String amountPaid;
    private String totalPayable;
    private String settlementAmount;
    private String paidOn;
    private String paymentStatus;
    private String paymentDescription;
    private String currency;
    private String paymentMethod;
    private Product product;
    private CardDetails cardDetails;
    private AccountDetails accountDetails;
    private AccountDetails[] accountPayments;
    private Customer customer;
    private Object metaData;


}
