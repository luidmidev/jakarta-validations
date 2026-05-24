package io.github.luidmidev.jakarta.validations;

import io.github.luidmidev.jakarta.validations.constraints.distinct.DistinctValidatorForArrays;
import io.github.luidmidev.jakarta.validations.constraints.distinct.DistinctValidatorForCollections;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DistinctValidatorTest {

    private final DistinctValidatorForCollections collectionValidator = new DistinctValidatorForCollections();
    private final DistinctValidatorForArrays arrayValidator = new DistinctValidatorForArrays();

    // --- Collections ---

    @Test
    void distinctCollection() {
        assertTrue(collectionValidator.isValid(List.of(1, 2, 3), null));
        assertTrue(collectionValidator.isValid(List.of("a", "b", "c"), null));
    }

    @Test
    void duplicateCollection() {
        assertFalse(collectionValidator.isValid(List.of(1, 1, 2), null));
        assertFalse(collectionValidator.isValid(List.of("a", "b", "a"), null));
    }

    @Test
    void nullCollectionIsValid() {
        assertTrue(collectionValidator.isValid(null, null));
    }

    @Test
    void emptyCollectionIsValid() {
        assertTrue(collectionValidator.isValid(List.of(), null));
    }

    @Test
    void singleElementCollectionIsValid() {
        assertTrue(collectionValidator.isValid(List.of("solo"), null));
    }

    @Test
    void setIsAlwaysDistinct() {
        // Sets cannot have duplicates, so this must always pass
        assertTrue(collectionValidator.isValid(Set.of(1, 2, 3), null));
    }

    // --- Arrays ---

    @Test
    void distinctArray() {
        assertTrue(arrayValidator.isValid(new Object[]{"a", "b", "c"}, null));
        assertTrue(arrayValidator.isValid(new Object[]{1, 2, 3}, null));
    }

    @Test
    void duplicateArray() {
        assertFalse(arrayValidator.isValid(new Object[]{"a", "a", "b"}, null));
        assertFalse(arrayValidator.isValid(new Object[]{1, 1, 2}, null));
    }

    @Test
    void nullArrayIsValid() {
        assertTrue(arrayValidator.isValid(null, null));
    }

    @Test
    void emptyArrayIsValid() {
        assertTrue(arrayValidator.isValid(new Object[]{}, null));
    }

    @Test
    void mixedNullsInArray() {
        // [null, null] has duplicates
        assertFalse(arrayValidator.isValid(new Object[]{null, null}, null));
        // [null, "a"] is distinct
        assertTrue(arrayValidator.isValid(new Object[]{null, "a"}, null));
    }
}
