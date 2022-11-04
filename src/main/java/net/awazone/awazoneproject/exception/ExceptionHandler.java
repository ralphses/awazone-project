package net.awazone.awazoneproject.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@ResponseStatus
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({
            IllegalUserException.class,
            UserNotActiveException.class,
            JwtException.class})
    public ResponseEntity<ResponseMessage> unauthorizedExceptionHandler(MainException mainException, WebRequest webRequest) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body(getResponseMessage(mainException.getMessage(), FORBIDDEN, webRequest));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            CustomInvalidParamException.class,
            NumberFormatException.class,
            KycDocumentException.class,
            FileStorageException.class,
            ConstraintViolationException.class,
            InsufficientResourceException.class,
            ResourceAlreadyExistException.class})
    public ResponseEntity<ResponseMessage> badRequestExceptionHandler(RuntimeException mainException, WebRequest webRequest) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(getResponseMessage(mainException.getMessage(), BAD_REQUEST, webRequest));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            UsernameNotFoundException.class,
            ResourceNotFoundException.class})
    public ResponseEntity<ResponseMessage> notFoundExceptionHandler(RuntimeException mainException, WebRequest webRequest) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(getResponseMessage(mainException.getMessage(), NOT_FOUND, webRequest));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            EmailNotSentException.class,
            UnsuccessfulRequestException.class})
    public ResponseEntity<ResponseMessage> internalServerExceptionHandler(MainException exception, WebRequest webRequest) {
        return ResponseEntity
                .status(OK)
                .body(getResponseMessage(exception.getMessage(), INTERNAL_SERVER_ERROR, webRequest));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            VirtualAccountException.class,
            CoinRemmitterException.class})
    public ResponseEntity<ResponseMessage> unprocessableRequest(MainException exception, WebRequest webRequest) {
        return ResponseEntity
                .status(UNPROCESSABLE_ENTITY)
                .body(getResponseMessage(exception.getMessage(), UNPROCESSABLE_ENTITY, webRequest));
    }

    private ResponseMessage getResponseMessage(String message, HttpStatus httpStatus, WebRequest webRequest) {
        return new ResponseMessage(message, httpStatus, new HashMap<>());
    }

}