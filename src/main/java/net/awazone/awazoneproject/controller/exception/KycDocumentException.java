package net.awazone.awazoneproject.controller.exception;

public class KycDocumentException extends MainException{
    public KycDocumentException(String message) {
        super(message);
    }

    public KycDocumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
