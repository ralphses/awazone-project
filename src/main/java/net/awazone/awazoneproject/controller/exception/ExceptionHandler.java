package net.awazone.awazoneproject.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@ResponseStatus
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalUserException.class)
    public ResponseEntity<ResponseMessage> unauthorizedExceptionHandler(MainException mainException, WebRequest webRequest) {
        return ResponseEntity.status(FORBIDDEN).body(getResponseMessage(mainException.getMessage(), FORBIDDEN, webRequest));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ResponseMessage> invalidTokenExceptionHandler(InvalidTokenException invalidTokenException, WebRequest webRequest) {
        return ResponseEntity.status(BAD_REQUEST).body(getResponseMessage(invalidTokenException.getMessage(), BAD_REQUEST, webRequest));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({UsernameAlreadyExistsException.class, KycDocumentException.class, FileStorageException.class})
    public ResponseEntity<ResponseMessage> usernameAlreadyExistsException(MainException mainException, WebRequest webRequest) {
        return ResponseEntity.status(BAD_REQUEST).body(getResponseMessage(mainException.getMessage(), BAD_REQUEST, webRequest));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({UserNotActiveException.class, PasswordNotMatchException.class, DuplicateUserRoleException.class, ConstraintViolationException.class})
    public ResponseEntity<ResponseMessage> accountNotActivatedException(MainException mainException, WebRequest webRequest) {
        return ResponseEntity.status(BAD_REQUEST).body(getResponseMessage(mainException.getMessage(), BAD_REQUEST, webRequest));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            CustomRoleNotFoundException.class,
            CustomFileNotFoundException.class,
            InvalidDomainException.class,
            AiboAccountNotFoundException.class})
    public ResponseEntity<ResponseMessage> notFoundException(MainException mainException, WebRequest webRequest) {
        return ResponseEntity.status(NOT_FOUND).body(getResponseMessage(mainException.getMessage(), NOT_FOUND, webRequest));
    }

    private ResponseMessage getResponseMessage(String message, HttpStatus httpStatus, WebRequest webRequest) {
        return new ResponseMessage(message, httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmailNotSentException.class)
    public ResponseEntity<ResponseMessage> failedOperation(Exception exception, WebRequest webRequest) {
        return ResponseEntity.status(OK).body(getResponseMessage(exception.getMessage(), INTERNAL_SERVER_ERROR, webRequest));
    }

}