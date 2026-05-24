package io.github.luidmidev.jakarta.validations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileSizeValidatorTest {

    @Test
    void withinKilobytes() {
        assertTrue(Validations.validFileSize(100, 1, FileSize.Unit.KB));    // 100B ≤ 1024B
        assertTrue(Validations.validFileSize(1024, 1, FileSize.Unit.KB));   // exactly 1KB
        assertFalse(Validations.validFileSize(1025, 1, FileSize.Unit.KB));  // 1025B > 1024B
    }

    @Test
    void withinMegabytes() {
        assertTrue(Validations.validFileSize(1_000_000, 2, FileSize.Unit.MB));   // ~1MB ≤ 2MB
        assertFalse(Validations.validFileSize(3_000_000, 2, FileSize.Unit.MB));  // 3MB > 2MB
    }

    @Test
    void zeroSizeAlwaysValid() {
        assertTrue(Validations.validFileSize(0, 5, FileSize.Unit.MB));
        assertTrue(Validations.validFileSize(0, 0, FileSize.Unit.B));
    }

    @Test
    void unitMultipliers() {
        assertEquals(1L, FileSize.Unit.B.multiplier());
        assertEquals(1_024L, FileSize.Unit.KB.multiplier());
        assertEquals(1_048_576L, FileSize.Unit.MB.multiplier());
        assertEquals(1_073_741_824L, FileSize.Unit.GB.multiplier());
        assertEquals(1_099_511_627_776L, FileSize.Unit.TB.multiplier());
    }

    @Test
    void fractionalMaxSize() {
        // 0.5 KB = 512 bytes
        assertTrue(Validations.validFileSize(512, 0.5f, FileSize.Unit.KB));
        assertFalse(Validations.validFileSize(513, 0.5f, FileSize.Unit.KB));
    }
}
