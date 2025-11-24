package io.github.luidmidev.jakarta.validations.constraints.contenttype;

import io.github.luidmidev.jakarta.validations.ContentType;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class ContentTypeValidatorForArraysOfByte implements ConstraintValidator<ContentType, byte[]> {

    private String[] allowedContentTypes;

    @Override
    public void initialize(ContentType constraintAnnotation) {
        this.allowedContentTypes = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(byte[] file, ConstraintValidatorContext context) {
        return Validations.isValidContentType(file, allowedContentTypes);
    }
}