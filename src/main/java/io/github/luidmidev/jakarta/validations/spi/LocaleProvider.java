package io.github.luidmidev.jakarta.validations.spi;

import java.util.Locale;

/**
 * SPI for locale resolution used by this library's validators.
 *
 * <p>Register a single implementation via the standard Java {@link java.util.ServiceLoader} mechanism:
 * create the file</p>
 * <pre>
 * META-INF/services/io.github.luidmidev.jakarta.validations.spi.LocaleProvider
 * </pre>
 * <p>containing the fully-qualified name of your implementation class. If no provider is found,
 * {@link Locale#getDefault()} is used as the fallback.</p>
 *
 * <h3>Spring example</h3>
 * <pre>{@code
 * // src/main/java/com/myapp/SpringLocaleProvider.java
 * public class SpringLocaleProvider implements LocaleProvider {
 *     @Override
 *     public Locale getLocale() {
 *         return org.springframework.context.i18n.LocaleContextHolder.getLocale();
 *     }
 * }
 * }</pre>
 * <pre>
 * # src/main/resources/META-INF/services/io.github.luidmidev.jakarta.validations.spi.LocaleProvider
 * com.myapp.SpringLocaleProvider
 * </pre>
 *
 */
@FunctionalInterface
public interface LocaleProvider {

    /**
     * Returns the locale to use for the current context (request, thread, etc.).
     *
     * @return the resolved {@link Locale}, never {@code null}
     */
    Locale getLocale();
}
