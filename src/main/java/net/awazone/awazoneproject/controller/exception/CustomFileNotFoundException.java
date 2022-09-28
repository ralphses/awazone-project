package net.awazone.awazoneproject.controller.exception;

public class CustomFileNotFoundException extends MainException{
    public CustomFileNotFoundException(String message) {
        super(message);
    }

    public CustomFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
