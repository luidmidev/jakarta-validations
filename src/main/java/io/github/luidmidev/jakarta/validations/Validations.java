package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.structs.ThrowableFunction;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.IntStream;

public final class Validations {

    private static final Tika TIKA = new Tika();

    private Validations() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValidCi(String cedulaString) {
        try {
            if (cedulaString == null) return false;
            if (cedulaString.length() != 10) return false;

            int[] cedula = cedulaString.chars().map(Character::getNumericValue).toArray();

            int sum = IntStream.range(0, 9)
                    .map(i -> cedula[i] * (i % 2 == 0 ? 2 : 1))
                    .map(i -> i > 9 ? i - 9 : i)
                    .sum();

            int calculatedLastDigit = sum % 10 == 0 ? 0 : 10 - (sum % 10);

            return calculatedLastDigit == cedula[9];

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidContentType(MultipartFile file, String[] expectedContentTypes) throws IOException {
        if (file == null) return false;
        var bytes = IOUtils.toByteArray(file.getInputStream());
        var contentType = TIKA.detect(bytes, file.getOriginalFilename());

        return Arrays.asList(expectedContentTypes).contains(contentType);
    }

    public static boolean isValidContentType(File file, String[] expectedContentTypes) throws IOException {
        if (file == null) return false;
        var bytes = IOUtils.toByteArray(new FileInputStream(file));
        var contentType = TIKA.detect(bytes, file.getName());
        return Arrays.asList(expectedContentTypes).contains(contentType);
    }

    public static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNullOrEmpty(List<?> value) {
        return value == null || value.isEmpty();
    }

    public static boolean valueBetween(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean valueBetween(float value, float min, float max) {
        return value >= min && value <= max;
    }

    public static boolean valueBetween(double value, double min, double max) {
        return value >= min && value <= max;
    }


    public static <T> boolean isValidContenType(T file, ConstraintValidatorContext context, String[] allowedContentTypes, Function<T, String> fileName, ThrowableFunction<T, InputStream, IOException> inputStream) throws IOException {
        if (file == null) return true;

        var bytes = IOUtils.toByteArray(inputStream.apply(file));
        var name = fileName.apply(file);
        var contentType = TIKA.detect(bytes, name);

        if (!Arrays.asList(allowedContentTypes).contains(contentType)) {
            if (context instanceof ConstraintValidatorContextImpl c) {
                c.addMessageParameter("fileName", name);
                c.addMessageParameter("expectedContentTypes", allowedContentTypes);
                c.addMessageParameter("contentType", contentType);
            }
            return false;
        }

        return true;
    }

    public static <T> boolean isValidFileSize(T file, ConstraintValidatorContext context, float maxFileSize, FileSize.Unit unit, ToLongFunction<T> fileSize, Function<T, String> fileName) {
        if (file == null) return true;
        if (!valueBetween(fileSize.applyAsLong(file), 0, maxFileSize * unit.multiplier())) {
            if (context instanceof ConstraintValidatorContextImpl c) {
                c.addMessageParameter("fileName", fileName.apply(file));
                c.addMessageParameter("maxFileSize", maxFileSize);
            }
            return false;
        }

        return true;
    }


}