package io.github.luidmidev.jakarta.validations.constraints.adult;

import io.github.luidmidev.jakarta.validations.Adult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AdultValidatorForLocalDate implements ConstraintValidator<Adult, LocalDate> {

    private int min;

    @Override
    public void initialize(Adult constraintAnnotation) {
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) return false;
        var now = LocalDate.now();
        if (value.isAfter(now)) return false;
        return Period.between(value, now).getYears() >= min;
    }
}
