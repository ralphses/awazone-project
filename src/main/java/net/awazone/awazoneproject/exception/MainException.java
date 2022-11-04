package net.awazone.awazoneproject.exception;

public class MainException extends RuntimeException {

    public static final String INVALID_TOKEN_MESSAGE = "Invalid token";

    public MainException(String message) {
        super(message);
    }

    public MainException(String message, Throwable cause) {
        super(message, cause);
    }
}
