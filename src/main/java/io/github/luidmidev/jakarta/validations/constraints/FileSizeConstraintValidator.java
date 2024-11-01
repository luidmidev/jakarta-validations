package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.FileSize;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.File;


public class FileSizeConstraintValidator implements ConstraintValidator<FileSize, File> {

    private float maxFileSize;
    private FileSize.Unit unit;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        this.maxFileSize = constraintAnnotation.value();
        this.unit = constraintAnnotation.unit();
    }

    @Override
    public boolean isValid(File file, ConstraintValidatorContext context) {
        return Validations.isValidFileSize(
                file,
                context,
                maxFileSize,
                unit,
                File::length,
                File::getName
        );
    }


}