package net.awazone.awazoneproject.model.dtos.aibopay;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class NewPayWithBankTransferRequest {

    @NotBlank(message = "Provide transaction reference")
    private String transactionReference;
    private String bankCode;
}
