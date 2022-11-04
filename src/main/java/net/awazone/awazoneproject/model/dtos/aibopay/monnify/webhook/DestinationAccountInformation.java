package net.awazone.awazoneproject.model.dtos.aibopay.monnify.webhook;

import lombok.Data;

@Data
public class DestinationAccountInformation {
    private String bankCode;
    private String bankName;
    private String accountNumber;
}
