package io.github.luidmidev.jakarta.validations.constraints;


import io.github.luidmidev.jakarta.validations.EquatorCi;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EquatorCiValidator implements ConstraintValidator<EquatorCi, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) return true;
        return Validations.isValidCi(s);
    }

}