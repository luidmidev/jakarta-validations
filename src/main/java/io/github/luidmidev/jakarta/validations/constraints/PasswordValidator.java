package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.Password;
import io.github.luidmidev.jakarta.validations.utils.LocaleContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final Map<Locale, Properties> PROPERTIES = new HashMap<>();
    private static final Properties DEFAULT_PROPERTIES = new Properties();
    private static final String RESOURCE_PREFIX = "passay";
    private static final Locale[] availableLocales = Locale.getAvailableLocales();

    private List<? extends Rule> rules;
    private String defaultMessage;

    private static final Map<Class<? extends Supplier<List<? extends Rule>>>, List<? extends Rule>> RULES_CACHE = new ConcurrentHashMap<>();

    static {
        try {
            for (var locale : availableLocales) {

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
        var clazz = password.value();
        rules = RULES_CACHE.computeIfAbsent(clazz, new Function<>() {
            @Override
            public List<? extends Rule> apply(Class<? extends Supplier<List<? extends Rule>>> key) {
                try {
                    return key.getDeclaredConstructor().newInstance().get();
                } catch (Exception e) {
                    throw new RuntimeException("Error creating password rules supplier", e);
                }
            }
        });
        defaultMessage = password.message();

    }

    private org.passay.PasswordValidator buildPassswordValidator() {
        var props = PROPERTIES.getOrDefault(LocaleContext.getLocale(), DEFAULT_PROPERTIES);
        return new org.passay.PasswordValidator(new PropertiesMessageResolver(props), rules);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        var validator = buildPassswordValidator();

        var passwordData = new PasswordData(value);
        var result = validator.validate(passwordData);

        if (result.isValid()) return true;


        if (defaultMessage.isEmpty()) {
            context.disableDefaultConstraintViolation();
            for (var validationMessage : validator.getMessages(result)) {
                context.buildConstraintViolationWithTemplate(validationMessage).addConstraintViolation();
            }
            return false;
        }

        return false;
    }
}