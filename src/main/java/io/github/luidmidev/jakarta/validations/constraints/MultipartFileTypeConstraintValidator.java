package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.FileContentType;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;


public class MultipartFileTypeConstraintValidator implements ConstraintValidator<FileContentType, MultipartFile> {

    private String[] allowedContentTypes;

    @Override
    public void initialize(FileContentType constraintAnnotation) {
        this.allowedContentTypes = constraintAnnotation.value();
    }

    @SneakyThrows
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return Validations.isValidContenType(
                file,
                context,
                allowedContentTypes,
                MultipartFile::getOriginalFilename,
                MultipartFile::getInputStream
        );
    }
}