package io.github.luidmidev.jakarta.validations;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import io.github.luidmidev.jakarta.validations.spi.LocaleContext;
import org.apache.tika.Tika;
import org.jspecify.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Validations {

    private static final Set<String> ISO_COUNTRIES = Arrays.stream(Locale.getISOCountries()).collect(Collectors.toUnmodifiableSet());

    // Holders para dependencias opcionales — se inicializan solo al primer uso
    private static final class TikaHolder {
        static final Tika INSTANCE = new Tika();
    }

    private static final class PhoneNumberUtilHolder {
        static final PhoneNumberUtil INSTANCE = PhoneNumberUtil.getInstance();
    }

    // Coeficientes para validación de RUC
    private static final int[] COEFF_RUC_PUBLIC = {3, 2, 7, 6, 5, 4, 3, 2};
    private static final int[] COEFF_RUC_PRIVATE = {4, 3, 2, 7, 6, 5, 4, 3, 2};

    private Validations() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Valida un RUC ecuatoriano (13 dígitos).
     * Soporta los tres tipos:
     * <ul>
     *   <li>Persona natural (3.er dígito 0-5): los 10 primeros dígitos son una CI válida.</li>
     *   <li>Entidad pública (3.er dígito 6): módulo 11 con coeficientes [3,2,7,6,5,4,3,2].</li>
     *   <li>Persona jurídica privada (3.er dígito 9): módulo 11 con coeficientes [4,3,2,7,6,5,4,3,2].</li>
     * </ul>
     */
    public static boolean validEcuRUC(String ruc) {
        int[] digits = parseAndValidateDigits(ruc, 13);
        if (digits == null) return false;

        return switch (digits[2]) {
            // Persona natural: los 10 primeros dígitos forman una CI válida
            case 0, 1, 2, 3, 4, 5 -> validEcuCIChecksum(digits) && Integer.parseInt(ruc.substring(10)) >= 1;
            // Entidad pública: módulo 11, dígito verificador en posición 9 (índice 8)
            case 6 -> validateRucModulo11(digits, COEFF_RUC_PUBLIC, 8, ruc.substring(9));
            // Persona jurídica privada: módulo 11, dígito verificador en posición 10 (índice 9)
            case 9 -> validateRucModulo11(digits, COEFF_RUC_PRIVATE, 9, ruc.substring(10));
            default -> false;
        };
    }

    private static boolean validateRucModulo11(int[] digits, int[] coeff, int checkPos, String establishment) {
        int sum = IntStream.range(0, coeff.length)
            .map(i -> digits[i] * coeff[i])
            .sum();
        int rem = sum % 11;
        int verifier = rem == 0 ? 0 : 11 - rem;
        return verifier != 10 && verifier == digits[checkPos] && Integer.parseInt(establishment) >= 1;
    }

    public static boolean validEcuCI(String cedulaString) {
        int[] cedula = parseAndValidateDigits(cedulaString, 10);
        if (cedula == null) return false;
        return validEcuCIChecksum(cedula);
    }

    /** Módulo 10 sobre un array de dígitos ya validado y parseado. */
    private static boolean validEcuCIChecksum(int[] cedula) {
        int sum = IntStream.range(0, 9)
            .map(i -> cedula[i] * (i % 2 == 0 ? 2 : 1))
            .map(product -> product > 9 ? product - 9 : product)
            .sum();
        return (10 - sum % 10) % 10 == cedula[9];
    }

    private static int @Nullable [] parseAndValidateDigits(String value, int expectedLength) {
        if (value == null || value.length() != expectedLength) return null;
        if (!value.chars().allMatch(Character::isDigit)) return null;

        int[] digits = value.chars().map(Character::getNumericValue).toArray();

        int province = digits[0] * 10 + digits[1];
        if (province < 1 || province > 24) return null;
        return digits;
    }

    public static boolean validContentType(MultipartFile file, String[] expectedContentTypes) throws IOException {
        if (file == null) return false;
        try (var is = file.getInputStream()) {
            var contentType = TikaHolder.INSTANCE.detect(is, file.getOriginalFilename());
            return matchesContentType(contentType, expectedContentTypes);
        }
    }

    public static boolean validContentType(File file, String[] expectedContentTypes) throws IOException {
        if (file == null) return false;
        try (var fis = new FileInputStream(file)) {
            var contentType = TikaHolder.INSTANCE.detect(fis, file.getName());
            return matchesContentType(contentType, expectedContentTypes);
        }
    }

    public static boolean validContentType(byte[] prefix, String[] expectedContentTypes) {
        if (prefix == null) return false;
        var contentType = TikaHolder.INSTANCE.detect(prefix);
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
    public static boolean validImageSize(File imageFile, int maxWidth, int maxHeight) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("El archivo no es una imagen válida.");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        return width <= maxWidth && height <= maxHeight;
    }

    public static boolean nullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean nullOrEmpty(List<?> value) {
        return value == null || value.isEmpty();
    }

    public static boolean between(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean between(float value, float min, float max) {
        return value >= min && value <= max;
    }

    public static boolean between(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean validFileSize(long fileSize, float maxFileSize, FileSize.Unit unit) {
        return between(fileSize, 0, maxFileSize * unit.multiplier());
    }

    /**
     * Valida una placa vehicular ecuatoriana contra los tipos permitidos.
     *
     * @param plate        placa a validar (se espera en mayúsculas, ej. {@code "ABC-1234"})
     * @param allowedTypes tipos de placa aceptados
     * @return {@code true} si la placa coincide con al menos uno de los tipos indicados
     */
    public static boolean validEcuPlate(String plate, EcuPlate.Type[] allowedTypes) {
        if (plate == null || allowedTypes == null) return false;
        for (var type : allowedTypes) {
            if (type.matches(plate)) return true;
        }
        return false;
    }

    public static boolean validISOCountry(String isoCode) {
        if (isoCode == null) return false;
        return ISO_COUNTRIES.contains(isoCode);
    }

    public static boolean phoneNumberValid(String phoneNumber, String defaultRegion) {
        try {
            var phoneUtil = PhoneNumberUtilHolder.INSTANCE;
            var parsed = phoneUtil.parse(phoneNumber, defaultRegion);
            return phoneUtil.isValidNumber(parsed);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static boolean phoneNumberValid(String phoneNumber) {
        return phoneNumberValid(phoneNumber, LocaleContext.getLocale().getCountry());
    }


}
