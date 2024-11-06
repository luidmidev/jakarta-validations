package io.github.luidmidev.jakarta.validations.utils;

import java.util.Locale;
import java.util.function.Supplier;

public class LocaleContext {
    private static Supplier<Locale> LOCALE_SUPPLIER = Locale::getDefault;

    public static void setLocaleSupplier(Supplier<Locale> localeSupplier) {
        LocaleContext.LOCALE_SUPPLIER = localeSupplier;
    }

    public static Locale getLocale() {
        return LOCALE_SUPPLIER.get();
    }
}
