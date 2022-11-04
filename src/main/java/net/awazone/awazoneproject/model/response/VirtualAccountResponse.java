package net.awazone.awazoneproject.model.response;

import lombok.Data;

@Data
public class VirtualAccountResponse {

    private String contractCode;
    private String accountReference;
    private String accountName;
    private String currencyCode;
    private String customerEmail;
    private String customerName;
    private Account[] accounts;
    private String collectionChannel;
    private String reservationReference;
    private String reservedAccountType;
    private String status;
    private String createdOn;
    private Object[] incomeSplitConfig;
    private String bvn;
    private String restrictPaymentSource;

}
