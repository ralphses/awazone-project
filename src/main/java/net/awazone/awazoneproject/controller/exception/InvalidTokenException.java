package net.awazone.awazoneproject.controller.exception;

public class InvalidTokenException extends MainException{

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
