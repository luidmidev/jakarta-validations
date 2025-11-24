package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.NoProfanity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class NoProfanityValidator implements ConstraintValidator<NoProfanity, String> {

    private static final String[] PROFANITIES_EN = {
            "anal", "anus", "ass", "bastard", "bitch", "bollocks", "brothel", "cock", "cunt", "dick", "dildo", "dyke",
            "fag", "fat", "fucker", "fuck", "fucking", "gay", "homo", "idiot", "jackass", "jizz", "kike", "lesbian", "lipstick",
            "madhouse", "mothafucka", "motherfucker", "nigger", "penis", "piss", "pussy", "shit", "slut", "sodomy", "twat",
            "vagina", "whore", "wanker"
    };

    private static final String[] PROFANITIES_ES = {
            "anal", "culo", "puta", "mierda", "coño", "verga", "pendejo", "bastardo", "cabron", "zorra", "picha",
            "maricón", "maricon", "gilipollas", "imbécil", "imbecil", "polla", "puto", "cabrón", "cabron", "coger",
            "puta madre", "chinga", "culero", "naco", "mujerzuela", "gorda", "cojon", "concha", "mamada", "drogadicto",
            "putilla", "hijo de puta", "malparido", "pendejazo", "reverendo hijo de puta", "maldito", "carajo",
            "estúpido", "estupido", "pichón", "pichon", "te vas a la mierda", "muerto de hambre", "cabron de mierda",
            "cabrón de mierda", "cabrona", "hijueputa", "puta mierda", "madre que te parió", "madre que te pario",
            "chingado", "pendejito", "cagón", "cagon", "estúpido de mierda", "estupido de mierda",
            "pendejo de mierda", "maldito imbécil", "maldito imbecil", "maldito cabrón", "maldito cabron"
    };

    /**
     * Regex global optimizado:
     * <p>
     * (?<!\p{L})  → antes NO puede haber una letra unicode
     * ( ... )     → palabra o frase ofensiva, escapada con Pattern.quote()
     * (?!\p{L})   → después NO puede haber una letra unicode
     * <p>
     * Esto evita falsos positivos como "cálculo" (que contiene "culo"),
     * pero detecta correctamente palabras aisladas o con puntuación.
     */
    public static final Pattern PROFANITY_PATTERN;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("(?<!\\p{L})(");

        Stream.concat(
                Arrays.stream(PROFANITIES_EN),
                Arrays.stream(PROFANITIES_ES)
        ).map(Pattern::quote).forEach(word -> sb.append(word).append("|"));

        sb.setLength(sb.length() - 1); // remover último "|"
        sb.append(")(?!\\p{L})");

        PROFANITY_PATTERN = Pattern.compile(
                sb.toString(),
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        return !PROFANITY_PATTERN.matcher(value).find();
    }
}
