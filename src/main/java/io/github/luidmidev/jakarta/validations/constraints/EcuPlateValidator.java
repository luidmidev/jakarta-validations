package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.EcuPlate;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EcuPlateValidator implements ConstraintValidator<EcuPlate, String> {

    private EcuPlate.Type[] allowedTypes;

    @Override
    public void initialize(EcuPlate annotation) {
        this.allowedTypes = annotation.value();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) return true;
        return Validations.validEcuPlate(s, allowedTypes);
    }

}
