package net.awazone.awazoneproject.exception;

public class UnsuccessfulRequestException extends MainException{
    public UnsuccessfulRequestException(String message) {
        super(message);
    }

    public UnsuccessfulRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
