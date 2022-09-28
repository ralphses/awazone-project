package net.awazone.awazoneproject.controller.exception;

public class DuplicateUserRoleException extends MainException{
    public DuplicateUserRoleException(String message) {
        super(message);
    }

    public DuplicateUserRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
