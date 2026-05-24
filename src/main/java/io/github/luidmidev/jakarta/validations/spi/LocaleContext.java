package io.github.luidmidev.jakarta.validations.spi;

import java.util.Locale;
import java.util.ServiceLoader;

/**
 * Central locale accessor used by this library's validators (e.g. {@code @PhoneNumber}, {@code @SafePassword}).
 *
 * <p>The active {@link LocaleProvider} is resolved <em>once</em> at class-loading time via
 * {@link ServiceLoader} and never changes afterwards — no mutable state, no surprises.</p>
 *
 * <h2>Default behaviour</h2>
 * <p>If no {@link LocaleProvider} is registered on the classpath, {@link Locale#getDefault()} is used.</p>
 *
 * <h2>Registering a custom provider</h2>
 * <ol>
 *   <li>Implement {@link LocaleProvider} in your project.</li>
 *   <li>Create the file
 *       {@code META-INF/services/io.github.luidmidev.jakarta.validations.spi.LocaleProvider}
 *       containing the fully-qualified class name of your implementation.</li>
 * </ol>
 *
 * <h3>Spring example</h3>
 * <pre>{@code
 * // SpringLocaleProvider.java
 * public class SpringLocaleProvider implements LocaleProvider {
 *     @Override
 *     public Locale getLocale() {
 *         return org.springframework.context.i18n.LocaleContextHolder.getLocale();
 *     }
 * }
 * }</pre>
 * <pre>
 * # META-INF/services/io.github.luidmidev.jakarta.validations.spi.LocaleProvider
 * com.myapp.infra.SpringLocaleProvider
 * </pre>
 *
 * @see LocaleProvider
 */
public final class LocaleContext {

    private static final LocaleProvider PROVIDER = ServiceLoader.load(LocaleProvider.class)
        .findFirst()
        .orElse(Locale::getDefault);

    private LocaleContext() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Returns the current locale as resolved by the active {@link LocaleProvider}.
     *
     * @return the current {@link Locale}, never {@code null}
     */
    public static Locale getLocale() {
        assert PROVIDER != null;
        return PROVIDER.getLocale();
    }
}
