package io.github.luidmidev.jakarta.validations.constraints.distincs;


import io.github.luidmidev.jakarta.validations.Distincs;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;


public class DistincsValidatorForArrays implements ConstraintValidator<Distincs, Object[]> {

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Arrays.stream(value).distinct().count() == value.length;
    }

}