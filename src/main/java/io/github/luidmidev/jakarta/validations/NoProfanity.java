package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.NoProfanityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {
        NoProfanityValidator.class,
})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface NoProfanity {

    String message() default "{my.validation.constraints.NoProfanity.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}