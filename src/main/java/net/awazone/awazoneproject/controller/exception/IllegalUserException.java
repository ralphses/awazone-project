package net.awazone.awazoneproject.controller.exception;

public class IllegalUserException extends MainException{
    public IllegalUserException(String message) {
        super(message);
    }

    public IllegalUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
