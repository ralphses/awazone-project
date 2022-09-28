package net.awazone.awazoneproject.controller.exception;

public class InvalidTokenTypeException extends MainException{
    public InvalidTokenTypeException(String message) {
        super(message);
    }

    public InvalidTokenTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
