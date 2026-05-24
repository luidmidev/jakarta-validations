package io.github.luidmidev.jakarta.validations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ISOCountryValidatorTest {

    @Test
    void validCodes() {
        assertTrue(Validations.validISOCountry("US"));
        assertTrue(Validations.validISOCountry("EC"));
        assertTrue(Validations.validISOCountry("DE"));
        assertTrue(Validations.validISOCountry("JP"));
        assertTrue(Validations.validISOCountry("GB"));
    }

    @Test
    void invalidCodes() {
        assertFalse(Validations.validISOCountry("XX"));
        assertFalse(Validations.validISOCountry("A"));
        assertFalse(Validations.validISOCountry("USA")); // ISO-3 not supported
        assertFalse(Validations.validISOCountry("us")); // must be uppercase
        assertFalse(Validations.validISOCountry(""));
    }

    @Test
    void nullReturnsInvalid() {
        assertFalse(Validations.validISOCountry(null));
    }

    @Test
    void validatorReturnsTrue_forNull() {
        var v = new io.github.luidmidev.jakarta.validations.constraints.ISOCountryValidator();
        assertTrue(v.isValid(null, null));
    }

    @Test
    void validatorReturnsTrue_forValidCode() {
        var v = new io.github.luidmidev.jakarta.validations.constraints.ISOCountryValidator();
        assertTrue(v.isValid("EC", null));
    }

    @Test
    void validatorReturnsFalse_forInvalidCode() {
        var v = new io.github.luidmidev.jakarta.validations.constraints.ISOCountryValidator();
        assertFalse(v.isValid("XX", null));
    }
}
