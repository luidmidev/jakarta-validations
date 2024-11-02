package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.structs.DefaultPasswordRules;
import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Set;


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