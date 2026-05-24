package io.github.luidmidev.jakarta.validations.constraints.distinct;

import io.github.luidmidev.jakarta.validations.Distinct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class DistinctValidatorForArrays implements ConstraintValidator<Distinct, Object[]> {

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return Arrays.stream(value).distinct().count() == value.length;
    }

}
