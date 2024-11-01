package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.contenttype.ContentTypeValidatorForArraysOfByte;
import io.github.luidmidev.jakarta.validations.constraints.contenttype.ContentTypeValidatorForFile;
import io.github.luidmidev.jakarta.validations.constraints.contenttype.ContentTypeValidatorForMultipartFile;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {
        ContentTypeValidatorForMultipartFile.class,
        ContentTypeValidatorForFile.class,
        ContentTypeValidatorForArraysOfByte.class
})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ContentType {

    String message() default "{my.validation.constraints.ContentType.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value();

}