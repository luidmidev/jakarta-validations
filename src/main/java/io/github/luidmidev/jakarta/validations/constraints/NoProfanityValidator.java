package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.NoProfanity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoProfanityValidator implements ConstraintValidator<NoProfanity, String> {

    private static final String[] PROFANITIES_EN = {
            "anal", "anus", "ass", "bastard", "bitch", "bollocks", "brothel", "cock", "cunt", "dick", "dildo", "dyke",
            "fag", "fat", "fucker", "fuck", "fucking", "gay", "homo", "idiot", "jackass", "jizz", "kike", "lesbian", "lipstick",
            "madhouse", "mothafucka", "motherfucker", "nigger", "penis", "piss", "pussy", "shit", "slut", "sodomy", "twat",
            "vagina", "whore", "wanker"
    };

    private static final String[] PROFANITIES_ES = {
            "anal", "culo", "puta", "mierda", "coño", "verga", "pendejo", "bastardo", "cabron", "zorra", "picha",
            "maricón", "gilipollas", "imbécil", "polla", "puto", "cabrón", "coger", "puta madre", "chinga", "culero", "naco",
            "mujerzuela", "gorda", "cojon", "concha", "mamada", "drogadicto", "putilla", "puta madre", "hijo de puta",
            "malparido", "hijo de tal", "pendejazo", "reverendo hijo de puta", "maldito", "carajo", "estúpido", "pichón",
            "te vas a la mierda", "muerto de hambre", "cabron de mierda", "cabrón de mierda", "cabrona", "hijueputa", "puta madre que te parió",
            "mamon", "desgraciado", "imbécil", "mamón", "puta mierda", "hijueputa", "madre que te parió", "chingado",
            "pendejito", "cagón", "estúpido de mierda", "pendejo de mierda", "maldito imbécil", "maldito cabrón"
    };


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        var lowerValue = value.toLowerCase();

        for (var profinity : PROFANITIES_EN) {
            if (lowerValue.contains(profinity)) {
                return false;
            }
        }

        for (var profinity : PROFANITIES_ES) {
            if (lowerValue.contains(profinity)) {
                return false;
            }
        }

        return true;
    }
}
