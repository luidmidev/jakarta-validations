package io.github.luidmidev.jakarta.validations.constraints;


import io.github.luidmidev.jakarta.validations.EcuCi;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EcuCiValidator implements ConstraintValidator<EcuCi, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) return true;
        return Validations.isValidEcuCi(s);
    }

}