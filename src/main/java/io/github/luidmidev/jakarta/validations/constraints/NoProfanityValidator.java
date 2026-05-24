package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.NoProfanity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class NoProfanityValidator implements ConstraintValidator<NoProfanity, String> {

    private static final Pattern LEET_A = Pattern.compile("[@4]");
    private static final Pattern LEET_E = Pattern.compile("[3]");
    private static final Pattern LEET_I = Pattern.compile("[1!]");
    private static final Pattern LEET_O = Pattern.compile("[0]");
    private static final Pattern LEET_S = Pattern.compile("[$5]");
    private static final Pattern LEET_T = Pattern.compile("[7]");
    private static final Pattern LEET_B = Pattern.compile("[8]");

    public static final Pattern PROFANITY_PATTERN;

    static {
        String joined = Stream.concat(
                loadWords("profanities_en.txt"),
                loadWords("profanities_es.txt")
            )
            .map(NoProfanityValidator::normalize)
            .distinct()
            .sorted((a, b) -> Integer.compare(b.length(), a.length()))
            .map(Pattern::quote)
            .reduce((a, b) -> a + "|" + b)
            .orElse("(?!x)x");

        PROFANITY_PATTERN = Pattern.compile(
            "(?<!\\p{L})(" + joined + ")(?!\\p{L})",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );
    }

    private static Stream<String> loadWords(String resourceName) {
        var is = NoProfanityValidator.class.getClassLoader().getResourceAsStream(resourceName);
        if (is == null) return Stream.empty();
        try (var reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return reader.lines()
                .map(String::strip)
                .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                .toList()
                .stream();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        return !PROFANITY_PATTERN.matcher(normalize(value)).find();
    }

    /**
     * Normalizes input to cover common evasion variants:
     * removes diacritics, converts leet speak, and collapses repeated chars.
     */
    static String normalize(String input) {
        String noDiacritics = Normalizer.normalize(input, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}", "");

        String noLeet = LEET_B.matcher(
            LEET_T.matcher(
                LEET_S.matcher(
                    LEET_O.matcher(
                        LEET_I.matcher(
                            LEET_E.matcher(
                                LEET_A.matcher(noDiacritics).replaceAll("a")
                            ).replaceAll("e")
                        ).replaceAll("i")
                    ).replaceAll("o")
                ).replaceAll("s")
            ).replaceAll("t")
        ).replaceAll("b");

        return noLeet.replaceAll("(.)\\1{2,}", "$1$1");
    }
}
