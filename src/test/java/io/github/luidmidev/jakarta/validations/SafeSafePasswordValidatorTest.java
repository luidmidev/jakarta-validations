package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.utils.DefaultPasswordRules;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SafeSafePasswordValidatorTest {

    private static Validator validator;

    private static class Model {
        @SafePassword(DefaultPasswordRules.class)
        final String password;

        Model(String password) {
            this.password = password;
        }
    }

    @BeforeAll
    static void setup() {
        validator = Validation.byDefaultProvider().configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

    private boolean isValid(String password) {
        return validator.validate(new Model(password)).isEmpty();
    }

    @Test
    void validPassword() {
        assertTrue(isValid("Password1!"));
        assertTrue(isValid("Secure@Pass1"));
        assertTrue(isValid("My$ecure1Pass"));
    }

    @Test
    void nullIsValid() {
        assertTrue(isValid(null));
    }

    @Test
    void tooShort() {
        assertFalse(isValid("P@ss1"));   // 5 chars < 8
    }

    @Test
    void tooLong() {
        assertFalse(isValid("P@ssword12345678901234567890123")); // 31 chars > 30
    }

    @Test
    void missingDigit() {
        assertFalse(isValid("Password!"));
    }

    @Test
    void missingSpecialChar() {
        assertFalse(isValid("Password1"));
    }

    @Test
    void missingUppercase() {
        assertFalse(isValid("password1!"));
    }

    @Test
    void missingLowercase() {
        assertFalse(isValid("PASSWORD1!"));
    }

    @Test
    void violationsContainMessages() {
        var violations = validator.validate(new Model("short"));
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }
}
