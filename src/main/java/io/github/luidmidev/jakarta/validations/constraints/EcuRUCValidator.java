package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.EcuRUC;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EcuRUCValidator implements ConstraintValidator<EcuRUC, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) return true;
        return Validations.validEcuRUC(s);
    }

}
