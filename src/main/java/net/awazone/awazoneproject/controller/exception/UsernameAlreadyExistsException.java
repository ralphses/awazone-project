package net.awazone.awazoneproject.controller.exception;

public class UsernameAlreadyExistsException extends MainException{
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

    public UsernameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
