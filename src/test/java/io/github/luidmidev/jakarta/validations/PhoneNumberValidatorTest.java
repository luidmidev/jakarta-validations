package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.PhoneNumberValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberValidatorTest {

    @Test
    void validUSNumber() {
        assertTrue(Validations.phoneNumberValid("+1 (415)-555-2671", "US"));
    }

    @Test
    void validSpainNumber() {
        assertTrue(Validations.phoneNumberValid("+34 612 345 678", "ES"));
    }

    @Test
    void validEcuadorNumber() {
        assertTrue(Validations.phoneNumberValid("+593 99 123 4567", "EC"));
    }

    @Test
    void invalidNumber() {
        assertFalse(Validations.phoneNumberValid("12345", "US"));
        assertFalse(Validations.phoneNumberValid("not-a-phone", "US"));
    }

    @Test
    void nullReturnsInvalid() {
        // libphonenumber throws on null, method catches NumberParseException
        assertFalse(Validations.phoneNumberValid(null, "US"));
    }

    @Test
    void validatorReturnsTrue_forNull() {
        var validator = new PhoneNumberValidator();
        assertTrue(validator.isValid(null, null));
    }

    @Test
    void validatorReturnsFalse_forGarbageInput() {
        var validator = new PhoneNumberValidator();
        assertFalse(validator.isValid("not-a-phone", null));
    }
}
