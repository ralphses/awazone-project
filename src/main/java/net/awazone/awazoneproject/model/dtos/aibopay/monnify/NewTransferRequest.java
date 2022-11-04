package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class NewTransferRequest {

    @NotBlank(message = "Password required")
    private String password;

    @NotBlank(message = "Amount required")
    @Pattern(regexp = "\\d+", message = "Invalid amount")
    private String amount;

    @NotBlank(message = "Source account number required")
    @Pattern(regexp = "\\d+", message = "Invalid accountNumber")
    @Length(max = 10, min = 10)
    private String sourceAccountNumber;

    @NotBlank(message = "Destination account number required")
    @Pattern(regexp = "\\d+", message = "Invalid accountNumber")
    @Length(max = 10, min = 10)
    private String destinationAccountNumber;

    @NotBlank(message = "Transfer purpose required")
    private String narration;

    @NotBlank(message = "Destination bank required")
    @Pattern(regexp = "\\d+", message = "Invalid bank")
    private String destinationBankCode;

    @NotBlank(message = "Currency not valid")
    private String currency;

}
