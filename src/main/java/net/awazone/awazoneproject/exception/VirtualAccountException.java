package net.awazone.awazoneproject.exception;

public class VirtualAccountException extends MainException{
    public VirtualAccountException(String message) {
        super(message);
    }

    public VirtualAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
