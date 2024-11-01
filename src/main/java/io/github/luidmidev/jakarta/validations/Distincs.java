package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.distincs.DistincsValidatorForArrays;
import io.github.luidmidev.jakarta.validations.constraints.distincs.DistincsValidatorForCollections;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {
        DistincsValidatorForCollections.class,
        DistincsValidatorForArrays.class
})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Distincs {

    String message() default "{my.validation.constraints.Distincs.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}