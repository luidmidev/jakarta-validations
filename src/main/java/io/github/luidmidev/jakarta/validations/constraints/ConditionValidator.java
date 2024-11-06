package io.github.luidmidev.jakarta.validations.constraints;

import io.github.luidmidev.jakarta.validations.Condition;
import io.github.luidmidev.jakarta.validations.utils.TemplateEvaluator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;

public class ConditionValidator implements ConstraintValidator<Condition, Object> {

    private String expression;

    @Override
    public void initialize(Condition constraintAnnotation) {
        this.expression = constraintAnnotation.condition();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {

            var map = Map.of("value", value);
            return TemplateEvaluator.evaluate(expression, map, Boolean.class);

        } catch (Exception e) {
            return false;
        }

    }
}
