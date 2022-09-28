package net.awazone.awazoneproject.controller.exception;

public class PasswordNotMatchException extends MainException{
    public PasswordNotMatchException(String message) {
        super(message);
    }

    public PasswordNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
