package io.github.luidmidev.jakarta.validations.utils;

import org.passay.rule.CharacterRule;
import org.passay.rule.LengthRule;
import org.passay.rule.Rule;

import java.util.List;
import java.util.function.Supplier;

import static org.passay.data.EnglishCharacterData.*;


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
