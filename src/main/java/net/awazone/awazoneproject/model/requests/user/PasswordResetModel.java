package net.awazone.awazoneproject.model.requests.user;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class PasswordResetModel {

    @NotEmpty
    @NotBlank
    @Length(min = 8)
    private String newPassword;

    @NotEmpty
    @NotBlank
    @Length(min = 8)
    private String confirmPassword;
}
