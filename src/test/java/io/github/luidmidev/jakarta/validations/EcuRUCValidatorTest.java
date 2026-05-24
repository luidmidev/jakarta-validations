package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.EcuRUCValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Algoritmos verificados manualmente:
 *
 * Persona natural "1713175071001":
 *   CI "1713175071" es válida + establecimiento "001" = 1 ≥ 1  ✓
 *
 * Entidad pública "1760000070001":
 *   coef=[3,2,7,6,5,4,3,2], sum=1·3+7·2+6·7=59, rem=59%11=4, ver=11-4=7
 *   digits[8]=7 ✓, establecimiento "0001"=1 ≥ 1 ✓
 *
 * Persona jurídica "1790000001001":
 *   coef=[4,3,2,7,6,5,4,3,2], sum=1·4+7·3+9·2=43, rem=43%11=10, ver=11-10=1
 *   digits[9]=1 ✓, establecimiento "001"=1 ≥ 1 ✓
 */
class EcuRUCValidatorTest {

    // --- RUC válidos ---
    private static final String NATURAL_PERSON_RUC = "1713175071001"; // persona natural
    private static final String PUBLIC_ENTITY_RUC  = "1760000070001"; // entidad pública
    private static final String PRIVATE_ENTITY_RUC = "1790000001001"; // persona jurídica privada

    @Test
    void validNaturalPerson() {
        assertTrue(Validations.validEcuRUC(NATURAL_PERSON_RUC));
    }

    @Test
    void validPublicEntity() {
        assertTrue(Validations.validEcuRUC(PUBLIC_ENTITY_RUC));
    }

    @Test
    void validPrivateEntity() {
        assertTrue(Validations.validEcuRUC(PRIVATE_ENTITY_RUC));
    }

    // --- Casos inválidos estructurales ---

    @Test
    void nullReturnsFalse() {
        assertFalse(Validations.validEcuRUC(null));
    }

    @Test
    void wrongLength() {
        assertFalse(Validations.validEcuRUC("123"));
        assertFalse(Validations.validEcuRUC("17900000010011")); // 14 dígitos
    }

    @Test
    void nonNumeric() {
        assertFalse(Validations.validEcuRUC("ABCDEFGHIJKLM"));
    }

    @Test
    void invalidProvince() {
        assertFalse(Validations.validEcuRUC("2590000001001")); // provincia 25 no existe
        assertFalse(Validations.validEcuRUC("0090000001001")); // provincia 00 no existe
    }

    @Test
    void invalidThirdDigit() {
        assertFalse(Validations.validEcuRUC("1770000000001")); // 3er dígito 7 → no permitido
        assertFalse(Validations.validEcuRUC("1780000000001")); // 3er dígito 8 → no permitido
    }

    @Test
    void zeroEstablishment() {
        assertFalse(Validations.validEcuRUC("1713175071000")); // natural, establecimiento 000
        assertFalse(Validations.validEcuRUC("1760000070000")); // pública, establecimiento 0000
        assertFalse(Validations.validEcuRUC("1790000001000")); // privada, establecimiento 000
    }

    // --- Dígito verificador incorrecto ---

    @Test
    void wrongCheckDigitNaturalPerson() {
        // CI inválida (dígito verificador cambiado)
        assertFalse(Validations.validEcuRUC("1713175079001")); // CI termina en 9, debería ser 1
    }

    @Test
    void wrongCheckDigitPublicEntity() {
        // dígito verificador en posición 9 cambiado (7 → 9)
        assertFalse(Validations.validEcuRUC("1760000090001"));
    }

    @Test
    void wrongCheckDigitPrivateEntity() {
        // dígito verificador en posición 10 cambiado (1 → 9)
        assertFalse(Validations.validEcuRUC("1790000009001"));
    }

    // --- Validador de constraint ---

    @Test
    void validatorReturnsTrue_forNull() {
        var v = new EcuRUCValidator();
        assertTrue(v.isValid(null, null));
    }

    @Test
    void validatorReturnsTrue_forValidRUC() {
        var v = new EcuRUCValidator();
        assertTrue(v.isValid(NATURAL_PERSON_RUC, null));
        assertTrue(v.isValid(PUBLIC_ENTITY_RUC, null));
        assertTrue(v.isValid(PRIVATE_ENTITY_RUC, null));
    }

    @Test
    void validatorReturnsFalse_forInvalidRUC() {
        var v = new EcuRUCValidator();
        assertFalse(v.isValid("1234567890123", null));
    }
}
