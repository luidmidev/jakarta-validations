package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.FileTypeConstraintValidator;
import io.github.luidmidev.jakarta.validations.constraints.MultipartFileTypeConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {
        MultipartFileTypeConstraintValidator.class,
        FileTypeConstraintValidator.class,
})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface FileContentType {

    String message() default "{my.validation.constraints.FileContentType.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value();

}