package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBvnRequest {

    @NotBlank(message = "username not provided")
    private String username;

    @NotBlank(message = "password not provided")
    private String password;

    @NotBlank(message = "Account number not provided")
    private String accountNumber;

    @NotBlank(message = "BVN not provided")
    private String bvn;

}
