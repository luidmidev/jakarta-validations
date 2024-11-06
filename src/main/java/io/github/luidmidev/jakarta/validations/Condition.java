package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.ConditionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ConditionValidator.class)
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface Condition {

    String condition();

    String message() default "{my.validation.constraints.Condition.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}