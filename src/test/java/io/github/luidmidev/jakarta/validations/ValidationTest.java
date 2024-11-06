package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.utils.DefaultPasswordRules;
import io.github.luidmidev.jakarta.validations.utils.TemplateEvaluator;
import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;


public class ValidationTest {

    @Test
    public void test() {
        var exampleModel = ExampleModel.builder()
                .value(null)
                .ci("1234567890")
                .image(new File("files/file1.jpeg"))
                .files(List.of(
                        new File("files/file1.jpeg"),
                        new File("files/file2.pdf")

                ))
                .password("123456")
                .url("wrong-url")
                .subModel(ExampleSubModel.builder()
                        .value(null)
                        .ci("1234567890")
                        .values(List.of("value1", "", "value1"))
                        .build()
                )
                .startsWithA("A")
                .build();

        try (
                var factory = Validation.byDefaultProvider().configure()
                        .messageInterpolator(new ParameterMessageInterpolator())
                        .buildValidatorFactory()
        ) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<ExampleModel>> violations = validator.validate(exampleModel);
            for (ConstraintViolation<ExampleModel> violation : violations) {
                System.out.println(violation.getPropertyPath() + ": " + violation.getMessage());
            }
        }

        Assertions.assertTrue(true);

    }


    @Test
    public void validPhoneNumber() {
        var phoneNumber = "+1 (415)-555-2671";
        Assertions.assertTrue(Validations.isMobileNumberValid(phoneNumber));
    }

    @Test
    void templateEvaluator0() {
        var parameters = Map.of("customer", new Customer("John"));
        String template = "Hello, ${customer.getName() == 'John' ? 'The John' : 'The other'}";
        String result = TemplateEvaluator.evaluate(template, parameters, String.class);
        System.out.println(result);
    }

    @Test
    void templateEvaluator1() {
        var parameters = Map.of("gen", Gender.MA);
        String template = "Hello, ${gen == 'MA' ? 'Mr.' : 'Mrs.'}";
        String result = TemplateEvaluator.evaluate(template, parameters, String.class);
        System.out.println(result);
    }

    @Test
    void templateEvaluator2() {
        var parameters = Map.of("map", Map.of("key", "value2"));
        String template = "Hello, ${map['key']}";
        String result = TemplateEvaluator.evaluate(template, parameters, String.class);
        System.out.println(result);
    }

    @Test
    void templateEvaluator3() {
        var parameters = Map.of("call", (Function<String, String>) String::toUpperCase);
        String template = "Hello, ${call.apply('hello')}";
        String result = TemplateEvaluator.evaluate(template, parameters, String.class);
        System.out.println(result);
    }

    @Test
    void templateEvaluator4() {
        var parameters = Map.of("call", (Function<String, String>) String::trim);
        String template = "Hello, ${call.apply(' hello ')}";
        String result = TemplateEvaluator.evaluate(template, parameters, String.class);
        System.out.println(result);
    }


    public enum Gender {
        MA,
        FE
    }

    @Data
    @AllArgsConstructor
    public static class Customer {
        private String name;
    }

    @Data
    @Builder
    public static class ExampleModel {

        @NotNull
        private Long value;

        @EquatorCi
        private String ci;

        @Image(width = 1871, height = 1323 + 1, dimensionValidation = Image.DimensionConstraint.EXACT)
        private File image;

        private List<@ContentType("image/png") @FileSize(value = 2f, unit = FileSize.Unit.B) File> files;

        @Password(DefaultPasswordRules.class)
        private String password;

        @NotBlank
        @URL
        private String url;


        @NotNull
        @Valid
        private ExampleSubModel subModel;

        @NotBlank
        @Condition(condition = "value.startsWith('A')", message = "Value must start with A")
        private String startsWithA;
    }

    @Data
    @Builder
    public static class ExampleSubModel {

        @NotNull
        private Long value;

        @EquatorCi
        private String ci;

        @NotEmpty
        private List<@NotBlank String> values;
    }

}