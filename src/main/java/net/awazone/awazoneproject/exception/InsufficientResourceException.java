package net.awazone.awazoneproject.exception;

public class InsufficientResourceException extends MainException{
    public InsufficientResourceException(String message) {
        super(message);
    }

    public InsufficientResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
