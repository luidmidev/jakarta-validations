package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.dimensions.ImageValidatorForArraysOfByte;
import io.github.luidmidev.jakarta.validations.constraints.dimensions.ImageValidatorForFile;
import io.github.luidmidev.jakarta.validations.constraints.dimensions.ImageValidatorForMultipartFile;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.Function;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {
        ImageValidatorForArraysOfByte.class,
        ImageValidatorForFile.class,
        ImageValidatorForMultipartFile.class
})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Image {

    String message() default "{my.validation.constraints.Image.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    long width() default 0;

    long height() default 0;

    DimensionConstraint dimensionValidation() default DimensionConstraint.NO_VALIDATION;

    enum DimensionConstraint {

        MAX(args -> args.widthObtained <= args.widthExpected && args.heightObtained <= args.heightExpected),
        MIN(args -> args.widthObtained >= args.widthExpected && args.heightObtained >= args.heightExpected),
        EXACT(args -> args.widthObtained == args.widthExpected && args.heightObtained == args.heightExpected),
        ASPECT_RATIO(args -> args.widthObtained / args.heightObtained == args.widthExpected / args.heightExpected),
        MAX_WIDTH(args -> args.widthObtained <= args.widthExpected),
        MIN_WIDTH(args -> args.widthObtained >= args.widthExpected),
        MAX_HEIGHT(args -> args.heightObtained <= args.heightExpected),
        MIN_HEIGHT(args -> args.heightObtained >= args.heightExpected),
        NO_VALIDATION(args -> true);

        private final Function<ArgsDimensionValidation, Boolean> validationFuntion;

        private final String message = "{my.validation.constraints.Image.DimensionValidation." + name() + ".message}";

        DimensionConstraint(Function<ArgsDimensionValidation, Boolean> validationFuntion) {
            this.validationFuntion = validationFuntion;
        }

        public boolean isValid(long widthObtained, long widthExpected, long heightObtained, long heightExpected) {
            return validationFuntion.apply(new ArgsDimensionValidation(widthObtained, widthExpected, heightObtained, heightExpected));
        }

        public String getMessage() {
            return message;
        }
    }

    record ArgsDimensionValidation(long widthObtained, long widthExpected, long heightObtained, long heightExpected) {
    }
}