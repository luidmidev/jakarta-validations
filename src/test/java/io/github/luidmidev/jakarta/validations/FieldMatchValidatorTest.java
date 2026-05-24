package io.github.luidmidev.jakarta.validations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FieldMatchValidatorTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = Validation.byDefaultProvider().configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();
    }

    // -------------------------------------------------------
    // Models
    // -------------------------------------------------------

    /**
     * Simple record — access via getter (record components have public accessors).
     */
    @FieldMatch(field = "password", matchingField = "confirmPassword")
    record PasswordForm(String password, String confirmPassword) {
    }

    /**
     * Multiple pairs on the same class using @Repeatable.
     */
    @FieldMatch(field = "password", matchingField = "confirmPassword")
    @FieldMatch(field = "email", matchingField = "confirmEmail")
    record MultiMatchForm(String password, String confirmPassword,
                          String email, String confirmEmail) {
    }

    /**
     * Class with private fields (no getters) — access via reflection fallback.
     */
    @FieldMatch(field = "a", matchingField = "b")
    static class PrivateFieldsForm {
        private final String a;
        private final String b;

        PrivateFieldsForm(String a, String b) {
            this.a = a;
            this.b = b;
        }
    }

    /**
     * Base class with a private field — used to verify hierarchy traversal.
     */
    static class BaseForm {
        private final String base;

        BaseForm(String base) {
            this.base = base;
        }
    }

    /**
     * Child class: 'base' lives in parent, 'confirm' lives here.
     */
    @FieldMatch(field = "base", matchingField = "confirm")
    static class ChildForm extends BaseForm {
        private final String confirm;

        ChildForm(String base, String confirm) {
            super(base);
            this.confirm = confirm;
        }
    }

    // -------------------------------------------------------
    // Helpers
    // -------------------------------------------------------

    private <T> Set<String> violationPaths(Set<ConstraintViolation<T>> violations) {
        return violations.stream()
            .map(v -> v.getPropertyPath().toString())
            .collect(Collectors.toSet());
    }

    // -------------------------------------------------------
    // Basic matching
    // -------------------------------------------------------

    @Test
    void matchingFields_isValid() {
        assertTrue(validator.validate(new PasswordForm("Secret1!", "Secret1!")).isEmpty());
    }

    @Test
    void nonMatchingFields_isInvalid() {
        var violations = validator.validate(new PasswordForm("Secret1!", "different"));
        assertFalse(violations.isEmpty());
    }

    @Test
    void violation_isReportedOnMatchingField() {
        var violations = validator.validate(new PasswordForm("Secret1!", "different"));
        assertTrue(violationPaths(violations).contains("confirmPassword"),
            "Violation should point to 'confirmPassword', got: " + violationPaths(violations));
    }

    @Test
    void bothNull_isValid() {
        // Objects.equals(null, null) == true
        assertTrue(validator.validate(new PasswordForm(null, null)).isEmpty());
    }

    @Test
    void oneNullOneNot_isInvalid() {
        assertFalse(validator.validate(new PasswordForm("Secret1!", null)).isEmpty());
        assertFalse(validator.validate(new PasswordForm(null, "Secret1!")).isEmpty());
    }

    @Test
    void nullObject_isValid() {
        // The validator itself receives null → returns true (null is valid; use @NotNull for non-null)
        assertTrue(validator.validate(new PasswordForm(null, null)).isEmpty());
    }

    // -------------------------------------------------------
    // @Repeatable — multiple pairs
    // -------------------------------------------------------

    @Test
    void multiMatch_allMatch_isValid() {
        var form = new MultiMatchForm("pass", "pass", "a@b.com", "a@b.com");
        assertTrue(validator.validate(form).isEmpty());
    }

    @Test
    void multiMatch_secondPairFails_reportedCorrectly() {
        var form = new MultiMatchForm("pass", "pass", "a@b.com", "wrong@b.com");
        var paths = violationPaths(validator.validate(form));
        assertFalse(paths.isEmpty());
        assertTrue(paths.contains("confirmEmail"));
        assertFalse(paths.contains("confirmPassword")); // first pair is fine
    }

    @Test
    void multiMatch_bothFail_bothReported() {
        var form = new MultiMatchForm("pass", "WRONG", "a@b.com", "wrong@b.com");
        var paths = violationPaths(validator.validate(form));
        assertTrue(paths.contains("confirmPassword"));
        assertTrue(paths.contains("confirmEmail"));
    }

    // -------------------------------------------------------
    // Reflection fallback (private fields, no getters)
    // -------------------------------------------------------

    @Test
    void privateFields_matching_isValid() {
        assertTrue(validator.validate(new PrivateFieldsForm("same", "same")).isEmpty());
    }

    @Test
    void privateFields_nonMatching_isInvalid() {
        assertFalse(validator.validate(new PrivateFieldsForm("aaa", "bbb")).isEmpty());
    }

    // -------------------------------------------------------
    // Inheritance — field search walks hierarchy
    // -------------------------------------------------------

    @Test
    void inheritedField_matching_isValid() {
        // 'base' is declared in PrivateFieldsForm (parent), 'confirm' in ChildForm
        assertTrue(validator.validate(new ChildForm("same", "same")).isEmpty());
    }

    @Test
    void inheritedField_nonMatching_isInvalid() {
        assertFalse(validator.validate(new ChildForm("same", "different")).isEmpty());
    }
}
