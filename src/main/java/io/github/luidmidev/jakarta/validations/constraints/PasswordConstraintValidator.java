package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    private static final Map<Locale, Properties> PROPERTIES = new HashMap<>();
    private static final Properties DEFAULT_PROPERTIES = new Properties();
    private static final String RESOURCE_PREFIX = "passay";
    private static Supplier<Locale> locale = Locale::getDefault;

    private List<? extends Rule> rules;

    static {
        try {
            for (Locale locale : Locale.getAvailableLocales()) {

                var inputStream = loadResource(RESOURCE_PREFIX + "_" + locale.getLanguage() + ".properties");
                if (inputStream == null) continue;

                Properties props = new Properties();
                props.load(inputStream);
                PROPERTIES.put(locale, props);

            }

            var inputStream = loadResource(RESOURCE_PREFIX + ".properties");
            DEFAULT_PROPERTIES.load(inputStream);


        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static InputStream loadResource(String constraintAnnotation) {
        return PasswordConstraintValidator.class.getClassLoader().getResourceAsStream(constraintAnnotation);
    }

    public static void setLocaleResolver(Supplier<Locale> locale) {
        PasswordConstraintValidator.locale = locale;
    }

    @Override
    public void initialize(Password password) {
        try {
            rules = password.value().getDeclaredConstructor().newInstance().get();
        } catch (InstantiationException | SecurityException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Invalid rule configuration", e);
        }
    }

    private PasswordValidator buildPassswordValidator() {
        var props = PROPERTIES.getOrDefault(locale.get(), DEFAULT_PROPERTIES);
        return new PasswordValidator(new PropertiesMessageResolver(props), rules);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        PasswordValidator validator = buildPassswordValidator();

        PasswordData passwordData = new PasswordData(value == null ? "" : value);
        RuleResult result = validator.validate(passwordData);

        if (result.isValid()) return true;

        context.disableDefaultConstraintViolation();

        for (String message : validator.getMessages(result)) {
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }

        return false;
    }

    static final Random random = new Random();
    static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    static final String DIGITS = "0123456789";
    static final String SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    static final String ALPHABET = UPPER_CASE + LOWER_CASE;
    static final List<String> list = List.of(UPPER_CASE, LOWER_CASE, DIGITS, SPECIAL, ALPHABET);

    public static String generateStrongPassword() {
        var password = new StringBuilder();
        for (int i = 0; i < 25; i++) {
            var index = random.nextInt(list.size());
            var character = list.get(index).charAt(random.nextInt(list.get(index).length()));
            password.append(character);
        }
        return password.toString();
    }

}