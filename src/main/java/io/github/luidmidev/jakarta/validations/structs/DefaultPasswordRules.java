package io.github.luidmidev.jakarta.validations.structs;

import org.passay.CharacterRule;
import org.passay.LengthRule;
import org.passay.Rule;

import java.util.List;
import java.util.function.Supplier;

import static org.passay.EnglishCharacterData.*;

public class DefaultPasswordRules implements Supplier<List<? extends Rule>> {
    @Override
    public List<? extends Rule> get() {
        return List.of(
                new LengthRule(8, 30),
                new CharacterRule(Digit, 1),
                new CharacterRule(Special, 1),
                new CharacterRule(UpperCase, 1),
                new CharacterRule(LowerCase, 1)
        );
    }
}
