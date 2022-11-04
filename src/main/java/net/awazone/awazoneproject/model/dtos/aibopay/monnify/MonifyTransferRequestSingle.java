package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.Builder;

@Builder
public class MonifyTransferRequestSingle {

    private String amount;
    private String reference;
    private String narration;
    private String destinationBankCode;
    private String destinationAccountNumber;
    private String currency;
    private String sourceAccountNumber;
}
