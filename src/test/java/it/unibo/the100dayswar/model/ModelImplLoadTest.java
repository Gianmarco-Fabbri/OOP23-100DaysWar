package it.unibo.the100dayswar.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests error handling while loading a saved game.
 */
class ModelImplLoadTest {
    @TempDir
    private Path tempDir;

    @Test
    void missingSaveThrowsAnExplicitError() {
        final String missingSave = tempDir.resolve("missing-save.ser").toString();
        assertThrows(IllegalStateException.class, () -> new ModelImpl(Optional.of(missingSave)));
    }
}
