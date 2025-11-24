package io.github.luidmidev.jakarta.validations.constraints.contenttype;

import io.github.luidmidev.jakarta.validations.ContentType;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.File;
import java.io.IOException;

public class ContentTypeValidatorForFile implements ConstraintValidator<ContentType, File> {

    private String[] allowedContentTypes;

    @Override
    public void initialize(ContentType constraintAnnotation) {
        this.allowedContentTypes = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(File file, ConstraintValidatorContext context) {
        if (file == null) return true;
        try {
            return Validations.isValidContentType(file, allowedContentTypes);
        } catch (IOException e) {
            throw new RuntimeException("Error validating content type", e);
        }
    }
}