package io.github.luidmidev.jakarta.validations;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public final class Validations {

    private static final Tika TIKA = new Tika();
    private static final List<String> ISO_COUNTRIES = Arrays.stream(Locale.getISOCountries()).toList();

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

    public static boolean isValidContentType(byte[] file, String[] expectedContentTypes) {
        if (file == null) return false;
        var contentType = TIKA.detect(file);
        return Arrays.asList(expectedContentTypes).contains(contentType);
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

    public static boolean isValidFileSize(long fileSize, float maxFileSize, FileSize.Unit unit) {
        return valueBetween(fileSize, 0, maxFileSize * unit.multiplier());
    }

    public static boolean isValidISOCountry(String isoCode) {
        return ISO_COUNTRIES.contains(isoCode);
    }


}
