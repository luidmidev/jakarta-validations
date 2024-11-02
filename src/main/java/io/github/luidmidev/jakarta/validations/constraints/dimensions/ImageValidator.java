package io.github.luidmidev.jakarta.validations.constraints.dimensions;

import io.github.luidmidev.jakarta.validations.Image;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


public abstract class ImageValidator<T> implements ConstraintValidator<Image, T> {

    private long expectedWidth;
    private long expectedHeight;
    private Image.DimensionConstraint dimensionValidation;

    @Override
    public void initialize(Image annotation) {
        this.expectedWidth = annotation.width();
        this.expectedHeight = annotation.height();
        this.dimensionValidation = annotation.dimensionValidation();
    }

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
        if (value == null) return true;

        BufferedImage image = null;
        try {
            image = ImageIO.read(getInputStream(value));
            if (image == null) return false;
            var widthObtained = image.getWidth();
            var heightObtained = image.getHeight();

            var valid = dimensionValidation.isValid(widthObtained, expectedWidth, heightObtained, expectedHeight);
            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(dimensionValidation.getMessage())
                        .addConstraintViolation();
            }
            return valid;
        } catch (IOException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Error reading image file: " + e.getMessage())
                    .addConstraintViolation();
            return false;
        } finally {
            if (image != null) {
                image.flush();
            }
        }
    }

    public abstract InputStream getInputStream(T value) throws IOException;
}
