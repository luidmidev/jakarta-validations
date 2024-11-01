package io.github.luidmidev.jakarta.validations.constraints.contenttype;

import io.github.luidmidev.jakarta.validations.ContentType;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

import java.io.File;

public class ContentTypeValidatorForFile implements ConstraintValidator<ContentType, File> {

    private String[] allowedContentTypes;

    @Override
    public void initialize(ContentType constraintAnnotation) {
        this.allowedContentTypes = constraintAnnotation.value();
    }

    @SneakyThrows
    @Override
    public boolean isValid(File file, ConstraintValidatorContext context) {
        if (file == null) return true;
        return Validations.isValidContentType(file, allowedContentTypes);
    }
}