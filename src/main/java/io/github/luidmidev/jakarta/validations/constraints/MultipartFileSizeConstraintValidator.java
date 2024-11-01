package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.FileSize;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;


public class MultipartFileSizeConstraintValidator implements ConstraintValidator<FileSize, MultipartFile> {

    private float maxFileSize;
    private FileSize.Unit unit;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        this.maxFileSize = constraintAnnotation.value();
        this.unit = constraintAnnotation.unit();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return Validations.isValidFileSize(
                file,
                context,
                maxFileSize,
                unit,
                MultipartFile::getSize,
                MultipartFile::getOriginalFilename
        );
    }

}