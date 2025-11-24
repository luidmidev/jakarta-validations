package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.utils.DefaultPasswordRules;
import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Set;


class ValidationTest {

    @Test
    void test() {
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


    @Test
    void validPhoneNumber() {
        var phoneNumber = "+1 (415)-555-2671";
        Assertions.assertTrue(Validations.isMobileNumberValid(phoneNumber));
    }

    public enum Gender {
        MA,
        FE
    }

    // =====================
// Customer
// =====================
    public static class Customer {

        private String name;

        public Customer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    // =====================
// ExampleModel
// =====================
    public static class ExampleModel {

        @NotNull
        private Long value;

        @EquatorCi
        private String ci;

        @Image(width = 1871, height = 1324, dimensionValidation = Image.DimensionConstraint.EXACT)
        private File image;

        private List<
                @ContentType("image/png")
                @FileSize(value = 2f, unit = FileSize.Unit.B)
                        File
                > files;

        @Password(DefaultPasswordRules.class)
        private String password;

        @NotBlank
        @URL
        private String url;

        @NotNull
        @Valid
        private ExampleSubModel subModel;


        // ----- Constructor vacío -----
        public ExampleModel() {
        }

        // ----- Constructor completo -----
        public ExampleModel(Long value, String ci, File image, List<File> files,
                            String password, String url, ExampleSubModel subModel) {
            this.value = value;
            this.ci = ci;
            this.image = image;
            this.files = files;
            this.password = password;
            this.url = url;
            this.subModel = subModel;
        }

        // ----- Getters y Setters -----
        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }

        public String getCi() {
            return ci;
        }

        public void setCi(String ci) {
            this.ci = ci;
        }

        public File getImage() {
            return image;
        }

        public void setImage(File image) {
            this.image = image;
        }

        public List<File> getFiles() {
            return files;
        }

        public void setFiles(List<File> files) {
            this.files = files;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ExampleSubModel getSubModel() {
            return subModel;
        }

        public void setSubModel(ExampleSubModel subModel) {
            this.subModel = subModel;
        }


        // ============================
        // Builder
        // ============================
        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long value;
            private String ci;
            private File image;
            private List<File> files;
            private String password;
            private String url;
            private ExampleSubModel subModel;

            public Builder value(Long value) {
                this.value = value;
                return this;
            }

            public Builder ci(String ci) {
                this.ci = ci;
                return this;
            }

            public Builder image(File image) {
                this.image = image;
                return this;
            }

            public Builder files(List<File> files) {
                this.files = files;
                return this;
            }

            public Builder password(String password) {
                this.password = password;
                return this;
            }

            public Builder url(String url) {
                this.url = url;
                return this;
            }

            public Builder subModel(ExampleSubModel subModel) {
                this.subModel = subModel;
                return this;
            }

            public ExampleModel build() {
                return new ExampleModel(value, ci, image, files, password, url, subModel);
            }
        }
    }


    // =====================
// ExampleSubModel
// =====================
    public static class ExampleSubModel {

        @NotNull
        private Long value;

        @EquatorCi
        private String ci;

        @NotEmpty
        private List<@NotBlank String> values;

        // ----- Constructor vacío -----
        public ExampleSubModel() {
        }

        // ----- Constructor completo -----
        public ExampleSubModel(Long value, String ci, List<String> values) {
            this.value = value;
            this.ci = ci;
            this.values = values;
        }

        // ----- Getters y Setters -----
        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }

        public String getCi() {
            return ci;
        }

        public void setCi(String ci) {
            this.ci = ci;
        }

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }


        // ============================
        // Builder
        // ============================
        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long value;
            private String ci;
            private List<String> values;

            public Builder value(Long value) {
                this.value = value;
                return this;
            }

            public Builder ci(String ci) {
                this.ci = ci;
                return this;
            }

            public Builder values(List<String> values) {
                this.values = values;
                return this;
            }

            public ExampleSubModel build() {
                return new ExampleSubModel(value, ci, values);
            }
        }
    }
}