package io.github.luidmidev.jakarta.validations.constraints.adult;

import io.github.luidmidev.jakarta.validations.Adult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class AdultValidatorForDate implements ConstraintValidator<Adult, Date> {

    private int min;

    @Override
    public void initialize(Adult constraintAnnotation) {
        this.min = constraintAnnotation.minAge();
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) return false;
        var valueLcalDate = value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        var now = LocalDate.now();
        if (valueLcalDate.isAfter(now)) return false;
        return Period.between(valueLcalDate, now).getYears() >= min;
    }
}