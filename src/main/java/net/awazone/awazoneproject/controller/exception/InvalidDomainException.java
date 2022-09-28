package net.awazone.awazoneproject.controller.exception;

public class InvalidDomainException extends MainException{
    public InvalidDomainException(String message) {
        super(message);
    }

    public InvalidDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
