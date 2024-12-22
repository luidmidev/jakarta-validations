package io.github.luidmidev.jakarta.validations.constraints.filesize;

import io.github.luidmidev.jakarta.validations.FileSize;
import io.github.luidmidev.jakarta.validations.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;


public class FileSizeValidatorForMultipartFile implements ConstraintValidator<FileSize, MultipartFile> {

    private float maxFileSize;
    private FileSize.Unit unit;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        this.maxFileSize = constraintAnnotation.value();
        this.unit = constraintAnnotation.unit();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null) {
            return true;
        }
        return Validations.isValidFileSize(file.getSize(), maxFileSize, unit);
    }

}