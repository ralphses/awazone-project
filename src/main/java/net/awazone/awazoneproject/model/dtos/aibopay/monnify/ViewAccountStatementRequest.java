package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ViewAccountStatementRequest {

    @NotBlank(message = "Invalid account reference")
    private String accountReference;

    @NotBlank(message = "Invalid password")
    private String password;

}
