package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class NewInitPaymentRequest {

    private double amount;

    @NotBlank(message = "Customer name required")
    private String customerName;

    @NotBlank(message = "Email required")
    private String customerEmail;
    private String paymentDescription;
}
