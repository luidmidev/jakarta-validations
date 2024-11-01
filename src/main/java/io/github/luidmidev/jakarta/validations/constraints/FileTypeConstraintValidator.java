package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.FileContentType;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileTypeConstraintValidator implements ConstraintValidator<FileContentType, File> {

    private String[] allowedContentTypes;

    @Override
    public void initialize(FileContentType constraintAnnotation) {
        this.allowedContentTypes = constraintAnnotation.value();
    }

    @SneakyThrows
    @Override
    public boolean isValid(File file, ConstraintValidatorContext context) {
        if (file == null) return true;
        return validate(file, allowedContentTypes, context);
    }

    public static boolean validate(File file, String[] expectedContentTypes, ConstraintValidatorContext context) throws IOException {
        return Validations.isValidContenType(
                file,
                context,
                expectedContentTypes,
                File::getName,
                FileInputStream::new
        );
    }
}