package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.adult.AdultValidatorForDate;
import io.github.luidmidev.jakarta.validations.constraints.adult.AdultValidatorForLocalDate;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {
        AdultValidatorForDate.class,
        AdultValidatorForLocalDate.class
})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Adult {
    String message() default "{my.validation.constraints.Adult.message}";

    int min() default 18;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}