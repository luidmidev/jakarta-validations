package io.github.luidmidev.jakarta.validations.utils;

import java.util.Locale;
import java.util.function.Supplier;

public final class LocaleContext {

    private LocaleContext() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static Supplier<Locale> localeSupplier = Locale::getDefault;

    public static void setLocaleSupplier(Supplier<Locale> localeSupplier) {
        LocaleContext.localeSupplier = localeSupplier;
    }

    public static Locale getLocale() {
        return localeSupplier.get();
    }
}
