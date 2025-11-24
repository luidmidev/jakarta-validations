package io.github.luidmidev.jakarta.validations.constraints.contenttype;

import io.github.luidmidev.jakarta.validations.ContentType;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public class ContentTypeValidatorForMultipartFile implements ConstraintValidator<ContentType, MultipartFile> {

    private String[] allowedContentTypes;

    @Override
    public void initialize(ContentType constraintAnnotation) {
        this.allowedContentTypes = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        try {
            return Validations.isValidContentType(file, allowedContentTypes);
        } catch (IOException e) {
            throw new RuntimeException("Error validating content type", e);
        }
    }
}