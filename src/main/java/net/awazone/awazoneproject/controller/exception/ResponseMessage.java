package net.awazone.awazoneproject.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {

    public static final String USER_ALREADY_VERIFIED = "User already verified";
    public static final String USER_VERIFIED_SUCCESSFULLY = "User verified successfully";
    public static final String TOKEN_EXPIRED = "Token expired";
    public static final String USER_EXISTS = "User with this email already exists!";
    public static final String ILLEGAL_USER = "Unauthorized User";
    public static final String USER_DOES_NOT_EXIST = "Username not found";
    public static final String INVALID_USER_ID = "User with this ID not found";
    public static final String USER_NOT_ACTIVATED = "Account not activated";
    public static final String INVALID_TOKEN_TYPE = "Invalid token type";
    public static final String EMAIL_NOT_SENT = "Failed to send email";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successfully";
    public static final String PASSWORD_NOT_MATCH = "New password must match confirm password";
    public static final String USER_DEACTIVATED = "User account deactivated";
    public static final String ROLE_NAME_TAKEN = "Role with this name exists";
    public static final String ROLE_NOT_FOUND = "Role with this name does not exists";

    private String message;
    private HttpStatus httpStatus;

}
