package net.awazone.awazoneproject.exception;

public class ResourceAlreadyExistException extends MainException{
    public ResourceAlreadyExistException(String message) {
        super(message);
    }

    public ResourceAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
