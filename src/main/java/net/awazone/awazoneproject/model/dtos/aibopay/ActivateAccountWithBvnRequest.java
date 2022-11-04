package net.awazone.awazoneproject.model.dtos.aibopay;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ActivateAccountWithBvnRequest {

    @NotBlank
    private String accountNumber;

    @NotBlank
    private String bvn;

    @NotBlank
    private String banckAccountName;

    @NotBlank
    private String dateOfBirth;

    @NotBlank
    private String mobileNo;
}
