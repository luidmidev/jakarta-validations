package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.Password;
import io.github.luidmidev.jakarta.validations.utils.LocaleContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final Map<Locale, Properties> PROPERTIES = new HashMap<>();
    private static final Properties DEFAULT_PROPERTIES = new Properties();
    private static final String RESOURCE_PREFIX = "passay";
    private static final Locale[] availableLocales = Locale.getAvailableLocales();

    private List<? extends Rule> rules;

    static {
        try {
            for (Locale locale : availableLocales) {

                var inputStream = loadResource(RESOURCE_PREFIX + "_" + locale.getLanguage() + ".properties");
                if (inputStream == null) continue;

                var props = new Properties();
                props.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                PROPERTIES.put(locale, props);

            }

            var inputStream = loadResource(RESOURCE_PREFIX + ".properties");
            DEFAULT_PROPERTIES.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));


        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static InputStream loadResource(String constraintAnnotation) {
        return PasswordValidator.class.getClassLoader().getResourceAsStream(constraintAnnotation);
    }


    @Override
    public void initialize(Password password) {
        try {
            rules = password.value().getDeclaredConstructor().newInstance().get();
        } catch (InstantiationException | SecurityException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Invalid rule configuration", e);
        }
    }

    private org.passay.PasswordValidator buildPassswordValidator() {
        var props = PROPERTIES.getOrDefault(LocaleContext.getLocale(), DEFAULT_PROPERTIES);
        return new org.passay.PasswordValidator(new PropertiesMessageResolver(props), rules);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        org.passay.PasswordValidator validator = buildPassswordValidator();

        PasswordData passwordData = new PasswordData(value == null ? "" : value);
        RuleResult result = validator.validate(passwordData);

        if (result.isValid()) return true;

        context.disableDefaultConstraintViolation();

        for (var message : validator.getMessages(result)) {
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }

        return false;
    }
}