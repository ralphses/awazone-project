package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


@Builder
@Getter
@Setter
public class NewAccountRequest {

    @NotBlank(message = "Account type must not be blank")
    private String accountType;

    @NotBlank(message = "Customer full name must not be blank")
    private String customerFullName;

    @NotBlank(message = "Customer bvn must not be blank")
    @Length(max = 11, min = 11)
    private String bvn;
}
