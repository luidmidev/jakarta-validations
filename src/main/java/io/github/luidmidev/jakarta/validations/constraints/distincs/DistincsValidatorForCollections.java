package io.github.luidmidev.jakarta.validations.constraints.distincs;


import io.github.luidmidev.jakarta.validations.Distincs;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;


public class DistincsValidatorForCollections implements ConstraintValidator<Distincs, Collection<?>> {

    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.stream().distinct().count() == value.size();
    }

}