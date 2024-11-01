package io.github.luidmidev.jakarta.validations.constraints;


import io.github.luidmidev.jakarta.validations.Distincs;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class DistincsConstraintValidator implements ConstraintValidator<Distincs, List<?>> {

    @Override
    public boolean isValid(List<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.stream().distinct().count() == value.size();
    }

}