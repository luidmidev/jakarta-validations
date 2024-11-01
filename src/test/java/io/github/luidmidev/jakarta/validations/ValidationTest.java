package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.structs.DefaultPasswordRules;
import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Set;


public class ValidationTest {

    @Test
    public void test() {
        var exampleModel = ExampleModel.builder()
                .ci("1234567890")
                .file1(new File("files/file1.jpeg"))
                .files(List.of(
                        new File("files/file1.jpeg"),
                        new File("files/file2.pdf")

                ))
                .password("123456")
                .url("wrong-url")
                .build();

        try (var factory = Validation.buildDefaultValidatorFactory()) {
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

        @Ci
        private String ci;

        @FileContentType({"image/png", "image/jpeg"})
        private File file1;


        private List<@FileContentType("image/png") @FileSize(value = 2f, unit = FileSize.Unit.B) File> files;

        @Password(DefaultPasswordRules.class)
        private String password;

        @NotBlank
        @URL
        private String url;

    }

}