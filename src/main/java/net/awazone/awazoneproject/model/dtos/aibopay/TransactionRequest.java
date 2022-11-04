package net.awazone.awazoneproject.model.dtos.aibopay;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
public class TransactionRequest {
    @NotBlank
    private double amount;
    @NotBlank
    private String username;
    @NotBlank
    String transactionType;
    private String description;
}
