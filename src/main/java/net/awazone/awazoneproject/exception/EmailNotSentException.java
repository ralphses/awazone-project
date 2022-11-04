package net.awazone.awazoneproject.exception;

public class EmailNotSentException extends MainException{
    public EmailNotSentException(String message) {
        super(message);
    }

    public EmailNotSentException(String message, Throwable cause) {
        super(message, cause);
    }
}
