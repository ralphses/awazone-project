package net.awazone.awazoneproject.utility.validation;

import net.awazone.awazoneproject.repository.user.AwazoneUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<CustomValidEmail, String> {

    private static final String EMAIL_PATTERN = "^[_A-Za-z\\d-+]+(.[_A-Za-z\\d-]+)*@" + "[A-Za-z\\d-]+(.[A-Za-z\\d]+)*(.[A-Za-z]{2,})$";

    @Autowired
    private AwazoneUserRepository awazoneUserRepository;

    @Override
    public void initialize(CustomValidEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return validateEmail(email);
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches() && awazoneUserRepository.findByAwazoneUserContactEmail(email).isEmpty();
    }
}
