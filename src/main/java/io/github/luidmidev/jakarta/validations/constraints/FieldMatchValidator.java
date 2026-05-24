package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.FieldMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String field;
    private String matchingField;

    @Override
    public void initialize(FieldMatch annotation) {
        this.field = annotation.field();
        this.matchingField = annotation.matchingField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        try {
            var fieldValue = getProperty(value, field);
            var matchingValue = getProperty(value, matchingField);

            if (Objects.equals(fieldValue, matchingValue)) return true;

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(matchingField)
                    .addConstraintViolation();
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Reads a property value using getter convention first (getXxx / isXxx),
     * then falls back to direct field access traversing the class hierarchy.
     */
    private static Object getProperty(Object obj, String fieldName) throws Exception {
        var capitalized = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

        for (var prefix : new String[]{"get", "is"}) {
            try {
                return obj.getClass().getMethod(prefix + capitalized).invoke(obj);
            } catch (NoSuchMethodException ignored) {
                // try next prefix or fall back to field access
            }
        }

        var f = findField(obj.getClass(), fieldName);
        if (f == null) throw new NoSuchFieldException("Field '%s' not found in %s".formatted(fieldName, obj.getClass().getName()));
        var wasAccessible = f.canAccess(obj);
        f.setAccessible(true);
        try {
            return f.get(obj);
        } finally {
            if (!wasAccessible) f.setAccessible(false);
        }
    }

    /** Walks the class hierarchy to find a declared field by name. */
    private static Field findField(Class<?> clazz, String fieldName) {
        var current = clazz;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
}
