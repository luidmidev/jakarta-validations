package io.github.luidmidev.jakarta.validations.utils;

import com.sun.el.ExpressionFactoryImpl;
import jakarta.el.*;

import java.util.Map;

public class TemplateEvaluator {

    private static final ExpressionFactory factory = new ExpressionFactoryImpl();

    public static <T> T evaluate(String template, Map<String, ?> parameters, Class<T> clazz) {
        var templateContext = new StandardELContext(factory);

        parameters.forEach((param, value) -> {
            var valueExpression = factory.createValueExpression(templateContext, "${" + param + "}", Object.class);
            valueExpression.setValue(templateContext, value);
        });

        var templateContent = factory.createValueExpression(templateContext, template, clazz);
        return clazz.cast(templateContent.getValue(templateContext));
    }
}