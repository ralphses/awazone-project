package net.awazone.awazoneproject.controller.exception;

public class AiboAccountNotFoundException extends MainException{
    public AiboAccountNotFoundException(String message) {
        super(message);
    }

    public AiboAccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
