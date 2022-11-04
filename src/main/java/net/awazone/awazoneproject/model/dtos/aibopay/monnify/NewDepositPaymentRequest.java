package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class NewDepositPaymentRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String paymentPurpose;
}
