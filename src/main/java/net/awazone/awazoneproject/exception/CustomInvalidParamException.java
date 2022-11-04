package net.awazone.awazoneproject.exception;

public class CustomInvalidParamException extends MainException{
    public CustomInvalidParamException(String message) {
        super(message);
    }

    public CustomInvalidParamException(String message, Throwable cause) {
        super(message, cause);
    }
}
