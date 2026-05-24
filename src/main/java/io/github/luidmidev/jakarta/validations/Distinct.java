package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.distinct.DistinctValidatorForArrays;
import io.github.luidmidev.jakarta.validations.constraints.distinct.DistinctValidatorForCollections;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {
        DistinctValidatorForCollections.class,
        DistinctValidatorForArrays.class
})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Distinct {

    String message() default "{io.github.luidmidev.jakarta.validations.Distinct.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
