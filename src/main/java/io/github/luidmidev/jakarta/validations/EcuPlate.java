package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.EcuPlateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = EcuPlateValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface EcuPlate {

    String message() default "{io.github.luidmidev.jakarta.validations.EcuPlate.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** Plate types allowed. Defaults to all types. */
    Type[] value() default {Type.PRIVATE, Type.MOTORCYCLE, Type.DIPLOMATIC};

    enum Type {
        /** Private vehicle: ABC-1234 */
        PRIVATE("^[A-Z]{3}-\\d{4}$"),

        /** Motorcycle: ABC-123 */
        MOTORCYCLE("^[A-Z]{3}-\\d{3}$"),

        /** Diplomatic vehicle: CD-0042 */
        DIPLOMATIC("^CD-\\d{4}$");

        private final Pattern pattern;

        Type(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        public boolean matches(String plate) {
            return pattern.matcher(plate).matches();
        }
    }
}
