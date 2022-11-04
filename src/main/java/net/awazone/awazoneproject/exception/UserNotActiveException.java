package net.awazone.awazoneproject.exception;

public class UserNotActiveException extends MainException{
    public UserNotActiveException(String message) {
        super(message);
    }

    public UserNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
