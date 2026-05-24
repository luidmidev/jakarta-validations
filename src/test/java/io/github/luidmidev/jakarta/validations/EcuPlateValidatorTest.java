package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.EcuPlateValidator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EcuPlateValidatorTest {

    private static Validator validator;

    // Modelos para pruebas de anotación parametrizada
    private record AnyPlateModel(@EcuPlate String plate) {
    }

    private record PrivateOnlyModel(@EcuPlate(EcuPlate.Type.PRIVATE) String plate) {
    }

    private record MotorcycleOrPrivateModel(@EcuPlate({EcuPlate.Type.PRIVATE, EcuPlate.Type.MOTORCYCLE}) String plate) {
    }

    @BeforeAll
    static void setup() {
        validator = Validation.byDefaultProvider().configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();
    }

    // --------------------------------------------------
    // Validaciones estáticas por tipo
    // --------------------------------------------------

    @Test
    void validParticular() {
        assertTrue(Validations.validEcuPlate("ABC-1234", new EcuPlate.Type[]{EcuPlate.Type.PRIVATE}));
        assertTrue(Validations.validEcuPlate("PZK-0001", new EcuPlate.Type[]{EcuPlate.Type.PRIVATE}));
    }

    @Test
    void validMoto() {
        assertTrue(Validations.validEcuPlate("ABC-123", new EcuPlate.Type[]{EcuPlate.Type.MOTORCYCLE}));
        assertTrue(Validations.validEcuPlate("PZK-001", new EcuPlate.Type[]{EcuPlate.Type.MOTORCYCLE}));
    }

    @Test
    void validDiplomatic() {
        assertTrue(Validations.validEcuPlate("CD-0042", new EcuPlate.Type[]{EcuPlate.Type.DIPLOMATIC}));
        assertTrue(Validations.validEcuPlate("CD-0001", new EcuPlate.Type[]{EcuPlate.Type.DIPLOMATIC}));
    }

    @Test
    void nullReturnsFalse() {
        assertFalse(Validations.validEcuPlate(null, EcuPlate.Type.values()));
    }

    @Test
    void invalidFormats() {
        var all = EcuPlate.Type.values();
        assertFalse(Validations.validEcuPlate("AB-1234", all)); // solo 2 letras
        assertFalse(Validations.validEcuPlate("ABCD-1234", all)); // 4 letras
        assertFalse(Validations.validEcuPlate("ABC-12345", all)); // 5 dígitos
        assertFalse(Validations.validEcuPlate("ABC1234", all)); // sin guión
        assertFalse(Validations.validEcuPlate("abc-1234", all)); // minúsculas
        assertFalse(Validations.validEcuPlate("", all)); // vacío
        assertFalse(Validations.validEcuPlate("ABC-12X4", all)); // letras en dígitos
    }

    @Test
    void motoNotAcceptedAsParticular() {
        // "ABC-123" es moto, no debe pasar como particular
        assertFalse(Validations.validEcuPlate("ABC-123", new EcuPlate.Type[]{EcuPlate.Type.PRIVATE}));
    }

    @Test
    void particularNotAcceptedAsMoto() {
        // "ABC-1234" es particular, no debe pasar como moto
        assertFalse(Validations.validEcuPlate("ABC-1234", new EcuPlate.Type[]{EcuPlate.Type.MOTORCYCLE}));
    }

    // --------------------------------------------------
    // Anotación: @EcuPlate (todos por defecto)
    // --------------------------------------------------

    @Test
    void annotation_defaultAcceptsAll() {
        assertTrue(validator.validate(new AnyPlateModel("ABC-1234")).isEmpty()); // particular
        assertTrue(validator.validate(new AnyPlateModel("ABC-123")).isEmpty());  // moto
        assertTrue(validator.validate(new AnyPlateModel("CD-0042")).isEmpty());  // diplomático
    }

    @Test
    void annotation_defaultRejectsInvalid() {
        assertFalse(validator.validate(new AnyPlateModel("abc-1234")).isEmpty());
        assertFalse(validator.validate(new AnyPlateModel("AB-1234")).isEmpty());
    }

    @Test
    void annotation_nullIsValid() {
        assertTrue(validator.validate(new AnyPlateModel(null)).isEmpty());
    }

    // --------------------------------------------------
    // Anotación: @EcuPlate(Type.PARTICULAR) — solo particular
    // --------------------------------------------------

    @Test
    void annotation_particularOnly_accepts() {
        assertTrue(validator.validate(new PrivateOnlyModel("ABC-1234")).isEmpty());
    }

    @Test
    void annotation_particularOnly_rejectsMoto() {
        assertFalse(validator.validate(new PrivateOnlyModel("ABC-123")).isEmpty());
    }

    @Test
    void annotation_particularOnly_rejectsDiplomatic() {
        assertFalse(validator.validate(new PrivateOnlyModel("CD-0042")).isEmpty());
    }

    // --------------------------------------------------
    // Anotación: @EcuPlate({PARTICULAR, MOTO})
    // --------------------------------------------------

    @Test
    void annotation_multiType_acceptsParticular() {
        assertTrue(validator.validate(new MotorcycleOrPrivateModel("ABC-1234")).isEmpty());
    }

    @Test
    void annotation_multiType_acceptsMoto() {
        assertTrue(validator.validate(new MotorcycleOrPrivateModel("ABC-123")).isEmpty());
    }

    @Test
    void annotation_multiType_rejectsDiplomatic() {
        assertFalse(validator.validate(new MotorcycleOrPrivateModel("CD-0042")).isEmpty());
    }

    // --------------------------------------------------
    // Validador directo
    // --------------------------------------------------

    @Test
    void validatorReturnsTrue_forNull() {
        var v = new EcuPlateValidator();
        assertTrue(v.isValid(null, null));
    }
}
