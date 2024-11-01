package io.github.luidmidev.jakarta.validations.constraints.contenttype;

import io.github.luidmidev.jakarta.validations.ContentType;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;


public class ContentTypeValidatorForMultipartFile implements ConstraintValidator<ContentType, MultipartFile> {

    private String[] allowedContentTypes;

    @Override
    public void initialize(ContentType constraintAnnotation) {
        this.allowedContentTypes = constraintAnnotation.value();
    }

    @SneakyThrows
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return Validations.isValidContentType(file, allowedContentTypes);
    }
}