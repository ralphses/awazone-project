package net.awazone.awazoneproject.controller.exception;

import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class CustomMethodArgumentNotValidException extends MethodArgumentNotValidException {

    public CustomMethodArgumentNotValidException(MethodParameter parameter, BindingResult bindingResult) {
        super(parameter, bindingResult);
    }
}
