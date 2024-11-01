package io.github.luidmidev.jakarta.validations.constraints;


import io.github.luidmidev.jakarta.validations.ISOCountry;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ISOCountryValidator implements ConstraintValidator<ISOCountry, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) return true;
        return Validations.isValidISOCountry(s);
    }

}