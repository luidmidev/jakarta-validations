package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.FileSizeConstraintValidator;
import io.github.luidmidev.jakarta.validations.constraints.MultipartFileSizeConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {
        MultipartFileSizeConstraintValidator.class,
        FileSizeConstraintValidator.class,
})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface FileSize {

    String message() default "{my.validation.constraints.FileSize.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    float value();

    Unit unit() default Unit.B;

    @RequiredArgsConstructor
    enum Unit {
        B(1, "B"),
        KB(1_024L, "KB"),
        MB(1_048_576L, "MB"),
        GB(1_073_741_824L, "GB"),
        TB(1_099_511_627_776L, "TB");

        private final long multiplier;
        private final String abbreviation;

        public long multiplier() {
            return multiplier;
        }

        public String abbreviation() {
            return abbreviation;
        }
    }
}
