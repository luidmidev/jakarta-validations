package io.github.luidmidev.jakarta.validations;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import io.github.luidmidev.jakarta.validations.utils.LocaleContext;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

        return matchesContentType(contentType, expectedContentTypes);
    }

    public static boolean isValidContentType(File file, String[] expectedContentTypes) throws IOException {
        if (file == null) return false;
        var bytes = IOUtils.toByteArray(new FileInputStream(file));
        var contentType = TIKA.detect(bytes, file.getName());
        return matchesContentType(contentType, expectedContentTypes);
    }

    public static boolean isValidContentType(byte[] file, String[] expectedContentTypes) {
        if (file == null) return false;
        var contentType = TIKA.detect(file);
        return matchesContentType(contentType, expectedContentTypes);
    }

    private static boolean matchesContentType(String contentType, String[] expectedContentTypes) {
        for (var expected : expectedContentTypes) {
            if (expected.equals(contentType) || (expected.endsWith("/*") && contentType.startsWith(expected.replace("/*", "/")))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Valida si la imagen cumple con las dimensiones especificadas.
     *
     * @param imageFile El archivo de la imagen a validar.
     * @param maxWidth  El ancho máximo permitido.
     * @param maxHeight La altura máxima permitida.
     * @return true si la imagen cumple con las dimensiones, false en caso contrario.
     * @throws IOException Si ocurre un error al leer la imagen.
     */
    public static boolean isValidImageSize(File imageFile, int maxWidth, int maxHeight) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("El archivo no es una imagen válida.");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        return width <= maxWidth && height <= maxHeight;
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

    public static boolean isMobileNumberValid(String phoneNumber, String defaultRegion) {
        try {
            var phoneUtil = PhoneNumberUtil.getInstance();
            var swissNumberProto = phoneUtil.parse(phoneNumber, defaultRegion);
            //String regionCode = phoneUtil.getRegionCodeForNumber(swissNumberProto);
            return phoneUtil.isValidNumber(swissNumberProto);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static boolean isMobileNumberValid(String phoneNumber) {
        return isMobileNumberValid(phoneNumber, LocaleContext.getLocale().getCountry());
    }


}
