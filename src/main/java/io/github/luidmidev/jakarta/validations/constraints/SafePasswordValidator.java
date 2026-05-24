package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.SafePassword;
import io.github.luidmidev.jakarta.validations.spi.LocaleContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;
import org.passay.resolver.PropertiesMessageResolver;
import org.passay.rule.Rule;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class SafePasswordValidator implements ConstraintValidator<SafePassword, String> {

    private static final Map<Locale, Properties> PROPERTIES;
    private static final Properties DEFAULT_PROPERTIES = new Properties();
    private static final String RESOURCE_PREFIX = "passay";

    private List<? extends Rule> rules;
    private String defaultMessage;

    private static final Map<Class<? extends Supplier<List<? extends Rule>>>, List<? extends Rule>> RULES_CACHE = new ConcurrentHashMap<>();

    static {
        try {
            var temp = new HashMap<Locale, Properties>();
            for (var locale : Locale.getAvailableLocales()) {
                var inputStream = loadResource(RESOURCE_PREFIX + "_" + locale.getLanguage() + ".properties");
                if (inputStream == null) continue;

                var props = new Properties();
                props.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                temp.put(locale, props);
            }
            PROPERTIES = Collections.unmodifiableMap(temp);

            var inputStream = loadResource(RESOURCE_PREFIX + ".properties");
            DEFAULT_PROPERTIES.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static InputStream loadResource(String constraintAnnotation) {
        return SafePasswordValidator.class.getClassLoader().getResourceAsStream(constraintAnnotation);
    }


    @Override
    public void initialize(SafePassword safePassword) {
        var clazz = safePassword.value();
        rules = RULES_CACHE.computeIfAbsent(clazz, key -> {
            try {
                return key.getDeclaredConstructor().newInstance().get();
            } catch (Exception e) {
                throw new RuntimeException("Error creating password rules supplier", e);
            }
        });
        defaultMessage = safePassword.message();

    }

    private org.passay.PasswordValidator buildPasswordValidator() {
        var props = PROPERTIES.getOrDefault(LocaleContext.getLocale(), DEFAULT_PROPERTIES);
        return new org.passay.DefaultPasswordValidator(new PropertiesMessageResolver(props), rules);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        var validator = buildPasswordValidator();

        var passwordData = new PasswordData(value);
        var result = validator.validate(passwordData);

        if (result.isValid()) return true;


        if (defaultMessage.isEmpty()) {
            context.disableDefaultConstraintViolation();
            for (var validationMessage : result.getMessages()) {
                context.buildConstraintViolationWithTemplate(validationMessage).addConstraintViolation();
            }
            return false;
        }

        return false;
    }
}