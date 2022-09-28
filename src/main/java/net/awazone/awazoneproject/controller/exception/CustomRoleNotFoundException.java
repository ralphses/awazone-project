package net.awazone.awazoneproject.controller.exception;

public class CustomRoleNotFoundException extends MainException{
    public CustomRoleNotFoundException(String message) {
        super(message);
    }

    public CustomRoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
