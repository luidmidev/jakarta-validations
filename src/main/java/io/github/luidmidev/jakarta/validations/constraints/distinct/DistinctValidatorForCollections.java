package io.github.luidmidev.jakarta.validations.constraints.distinct;

import io.github.luidmidev.jakarta.validations.Distinct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

public class DistinctValidatorForCollections implements ConstraintValidator<Distinct, Collection<?>> {

    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value.stream().distinct().count() == value.size();
    }

}
