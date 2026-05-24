package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.FieldMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = FieldMatchValidator.class)
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Repeatable(FieldMatch.List.class)
public @interface FieldMatch {

    String message() default "{io.github.luidmidev.jakarta.validations.FieldMatch.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** Reference field (source of truth). */
    String field();

    /** Field that must match {@link #field()}. The constraint violation is reported here. */
    String matchingField();

    @Documented
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @interface List {
        FieldMatch[] value();
    }
}
