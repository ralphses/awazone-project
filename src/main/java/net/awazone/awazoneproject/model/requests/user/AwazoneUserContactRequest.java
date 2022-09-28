package net.awazone.awazoneproject.model.requests.user;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class AwazoneUserContactRequest {

    @NotNull
    @NotBlank
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotBlank
    @NotEmpty
    private String mobilePhone;

    private String otherPhone;
}
