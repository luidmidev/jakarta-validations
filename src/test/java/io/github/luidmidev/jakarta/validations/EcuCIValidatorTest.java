package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.EcuCIValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EcuCIValidatorTest {

    // 1,7,1,3,1,7,5,0,7 → sum=29 → check=1 ✓
    private static final String VALID_CI_1 = "1713175071";
    // 0,1,0,2,0,6,8,5,2 → sum=25 → check=5 ✓
    private static final String VALID_CI_2 = "0102068525";
    // 1,1,1,1,1,1,1,1,1 → sum=14 → check=6 ✓
    private static final String VALID_CI_3 = "1111111116";

    @Test
    void validCedulas() {
        assertTrue(Validations.validEcuCI(VALID_CI_1));
        assertTrue(Validations.validEcuCI(VALID_CI_2));
        assertTrue(Validations.validEcuCI(VALID_CI_3));
    }

    @Test
    void nullReturnsInvalid() {
        assertFalse(Validations.validEcuCI(null));
    }

    @Test
    void wrongLength() {
        assertFalse(Validations.validEcuCI("12345"));
        assertFalse(Validations.validEcuCI("12345678901"));
        assertFalse(Validations.validEcuCI(""));
    }

    @Test
    void wrongCheckDigit() {
        assertFalse(Validations.validEcuCI("1234567890")); // check expected 7, got 0
        assertFalse(Validations.validEcuCI("1713175070")); // check expected 1, got 0
    }

    @Test
    void nonNumeric() {
        assertFalse(Validations.validEcuCI("ABCDEFGHIJ"));
    }

    @Test
    void validatorReturnsTrue_forNull() {
        var validator = new EcuCIValidator();
        assertTrue(validator.isValid(null, null));
    }

    @Test
    void validatorReturnsTrue_forValidCi() {
        var validator = new EcuCIValidator();
        assertTrue(validator.isValid(VALID_CI_1, null));
    }

    @Test
    void validatorReturnsFalse_forInvalidCi() {
        var validator = new EcuCIValidator();
        assertFalse(validator.isValid("1234567890", null));
    }
}
