package io.github.luidmidev.jakarta.validations.constraints;


import io.github.luidmidev.jakarta.validations.PhoneNumber;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) return true;
        return Validations.isMobileNumberValid(s);
    }
}