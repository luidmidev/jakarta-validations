package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.utils.DefaultPasswordRules;
import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


class ValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = Validation.byDefaultProvider().configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

    @Test
    void modelWithKnownViolationsProducesExpectedErrors() {
        var model = ExampleModel.builder()
                .value(null)                        // @NotNull → violation
                .ci("1234567890")                   // @EcuCi   → wrong check digit
                .image(new File("files/file1.jpeg"))
                .files(List.of(
                        new File("files/file1.jpeg"),
                        new File("files/file2.pdf")
                ))
                .password("123456")                 // @SafePassword → too short, missing upper/special
                .url("wrong-url")                   // @URL     → violation
                .subModel(ExampleSubModel.builder()
                        .value(null)                // @NotNull → violation
                        .ci("1234567890")           // @EcuCi   → wrong check digit
                        .values(List.of("value1", "", "value1")) // @NotBlank on "" + @Distinct duplicate
                        .build()
                )
                .build();

        Set<ConstraintViolation<ExampleModel>> violations = validator.validate(model);

        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));

        var paths = violations.stream()
                .map(v -> v.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertFalse(violations.isEmpty(), "Expected constraint violations but got none");
        assertTrue(paths.contains("value"),    "Expected @NotNull violation on 'value'");
        assertTrue(paths.contains("ci"),       "Expected @EcuCi violation on 'ci'");
        assertTrue(paths.contains("password"), "Expected @SafePassword violation on 'password'");
        assertTrue(paths.contains("url"),      "Expected @URL violation on 'url'");
        assertTrue(paths.contains("subModel.value"), "Expected @NotNull violation on 'subModel.value'");
        assertTrue(paths.contains("subModel.ci"),    "Expected @EcuCi violation on 'subModel.ci'");
    }

    @Test
    void modelWithValidDataProducesNoErrors() {
        var model = ExampleModel.builder()
                .value(1L)
                .ci("1713175071")          // valid Ecuadorian CI
                .image(null)
                .files(List.of())
                .password("Password1!")    // passes DefaultPasswordRules
                .url("https://example.com")
                .subModel(ExampleSubModel.builder()
                        .value(42L)
                        .ci("1713175071")
                        .values(List.of("alpha", "beta", "gamma"))
                        .build()
                )
                .build();

        Set<ConstraintViolation<ExampleModel>> violations = validator.validate(model);
        assertTrue(violations.isEmpty(), "Expected no violations but got: " + violations);
    }

    @Test
    void validPhoneNumber() {
        assertTrue(Validations.phoneNumberValid("+1 (415)-555-2671", "US"));
    }


    // =====================
    // ExampleModel
    // =====================
    public static class ExampleModel {

        @NotNull
        private Long value;

        @EcuCI
        private String ci;

        @Image(width = 1871, height = 1324, dimensionValidation = Image.DimensionConstraint.EXACT)
        private File image;

        private List<
                @ContentType("image/png")
                @FileSize(value = 2f, unit = FileSize.Unit.B)
                        File
                > files;

        @SafePassword(DefaultPasswordRules.class)
        private String password;

        @NotBlank
        @URL
        private String url;

        @NotNull
        @Valid
        private ExampleSubModel subModel;

        public ExampleModel() {
        }

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

        public Long getValue() { return value; }
        public void setValue(Long value) { this.value = value; }
        public String getCi() { return ci; }
        public void setCi(String ci) { this.ci = ci; }
        public File getImage() { return image; }
        public void setImage(File image) { this.image = image; }
        public List<File> getFiles() { return files; }
        public void setFiles(List<File> files) { this.files = files; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public ExampleSubModel getSubModel() { return subModel; }
        public void setSubModel(ExampleSubModel subModel) { this.subModel = subModel; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long value;
            private String ci;
            private File image;
            private List<File> files;
            private String password;
            private String url;
            private ExampleSubModel subModel;

            public Builder value(Long value) { this.value = value; return this; }
            public Builder ci(String ci) { this.ci = ci; return this; }
            public Builder image(File image) { this.image = image; return this; }
            public Builder files(List<File> files) { this.files = files; return this; }
            public Builder password(String password) { this.password = password; return this; }
            public Builder url(String url) { this.url = url; return this; }
            public Builder subModel(ExampleSubModel subModel) { this.subModel = subModel; return this; }

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

        @EcuCI
        private String ci;

        @NotEmpty
        @Distinct
        private List<@NotBlank String> values;

        public ExampleSubModel() {
        }

        public ExampleSubModel(Long value, String ci, List<String> values) {
            this.value = value;
            this.ci = ci;
            this.values = values;
        }

        public Long getValue() { return value; }
        public void setValue(Long value) { this.value = value; }
        public String getCi() { return ci; }
        public void setCi(String ci) { this.ci = ci; }
        public List<String> getValues() { return values; }
        public void setValues(List<String> values) { this.values = values; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long value;
            private String ci;
            private List<String> values;

            public Builder value(Long value) { this.value = value; return this; }
            public Builder ci(String ci) { this.ci = ci; return this; }
            public Builder values(List<String> values) { this.values = values; return this; }

            public ExampleSubModel build() {
                return new ExampleSubModel(value, ci, values);
            }
        }
    }
}
