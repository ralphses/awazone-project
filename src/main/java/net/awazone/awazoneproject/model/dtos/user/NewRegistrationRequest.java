package net.awazone.awazoneproject.model.dtos.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NewRegistrationRequest {

    @NotNull(message = "Full name must not be null")
    @NotEmpty(message = "Full name must not be empty")
    @Length(min = 3, message = "Full name must not be less than 3 characters")
    String fullName;

    @NotEmpty
    @NotNull
    @Email
    String email;

    @NotNull
    @NotEmpty
    @Length(min = 5)
    String password;
    String confirmPassword;

    String referrer;

}
