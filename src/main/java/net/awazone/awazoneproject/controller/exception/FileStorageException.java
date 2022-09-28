package net.awazone.awazoneproject.controller.exception;

public class FileStorageException extends MainException{
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
