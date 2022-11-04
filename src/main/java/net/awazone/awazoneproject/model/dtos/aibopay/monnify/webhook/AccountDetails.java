package net.awazone.awazoneproject.model.dtos.aibopay.monnify.webhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDetails {
    private String accountName;
    private String accountNumber;
    private String bankCode;
    private String amountPaid;
}
