package io.github.luidmidev.jakarta.validations.constraints;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NoProfanityValidatorTest {

    private final NoProfanityValidator validator = new NoProfanityValidator();

    private boolean valid(String text) {
        return validator.isValid(text, null);
    }

    // -----------------------------------------
    //   PRUEBAS DE CASOS CORRECTOS (NO MATCH)
    // -----------------------------------------

    @Test
    @DisplayName("No debe detectar palabras inocentes con subcadenas ofensivas")
    void testFalsePositives() {
        assertTrue(valid("cálculo"));                  // contiene "culo" pero NO es ofensivo
        assertTrue(valid("estupendamente"));           // contiene "estupenda", NO "estúpido"
        assertTrue(valid("vergalarga"));               // contiene "verga", pero no es palabra sola
        assertTrue(valid("Pensamiento profundo"));     // contiene "pene" parcialmente
        assertTrue(valid("El conchal es un sitio"));   // contiene "concha"
        assertTrue(valid("El cálculo integral es útil"));
        assertTrue(valid("Mi amigo se llama Veronica")); // "verga" no debe matchear dentro
    }

    // -----------------------------------------
    //         PRUEBAS DE PALABRAS SIMPLES
    // -----------------------------------------

    @Test
    @DisplayName("Debe detectar insultos simples en español")
    void testSimpleSpanishProfanities() {
        assertFalse(valid("Ese tipo es un pendejo"));
        assertFalse(valid("Qué mierda"));
        assertFalse(valid("coño"));
        assertFalse(valid("verga"));
        assertFalse(valid("imbécil"));
        assertFalse(valid("puto"));
    }

    @Test
    @DisplayName("Debe detectar insultos simples en inglés")
    void testSimpleEnglishProfanities() {
        assertFalse(valid("fuck"));
        assertFalse(valid("Ass"));
        assertFalse(valid("Bitch"));
        assertFalse(valid("cock"));
        assertFalse(valid("slut"));
        assertFalse(valid("dick"));
    }

    // -----------------------------------------
    //      MULTIPLES PALABRAS / FRASES
    // -----------------------------------------

    @Test
    @DisplayName("Debe detectar insultos compuestos en español")
    void testComplexSpanishProfanities() {
        assertFalse(valid("hijo de puta"));
        assertFalse(valid("cabron de mierda"));
        assertFalse(valid("madre que te pario"));
        assertFalse(valid("te vas a la mierda"));
        assertFalse(valid("maldito imbecil"));
        assertFalse(valid("pendejo de mierda"));
    }

    @Test
    @DisplayName("Debe detectar insultos compuestos con puntuación")
    void testProfanitiesWithPunctuation() {
        assertFalse(valid("pendejo, ¿qué haces?"));
        assertFalse(valid("mierda..."));
        assertFalse(valid("puta madre!"));
        assertFalse(valid("hijo de puta."));
    }

    // -----------------------------------------
    //              VARIACIONES
    // -----------------------------------------

    @Test
    @DisplayName("Debe detectar palabras con acentos y sin acentos")
    void testAccents() {
        assertFalse(valid("imbécil"));
        assertFalse(valid("imbecil"));
        assertFalse(valid("cabrón"));
        assertFalse(valid("cabron"));
        assertFalse(valid("estúpido de mierda"));
        assertFalse(valid("estupido de mierda"));
    }

    // -----------------------------------------
    //           CASOS NEUTROS
    // -----------------------------------------

    @Test
    @DisplayName("Debe permitir textos sin groserías")
    void testGoodSentences() {
        assertTrue(valid("Hola, ¿cómo estás?"));
        assertTrue(valid("Este sistema valida contenido"));
        assertTrue(valid("El cielo es azul"));
        assertTrue(valid("Necesito ayuda con programación"));
        assertTrue(valid("Las matemáticas pueden ser difíciles"));
    }

    // -----------------------------------------
    //          NULOS Y ESPACIOS VACÍOS
    // -----------------------------------------

    @Test
    @DisplayName("Debe aceptar nulos y cadenas vacías")
    void testNullAndBlank() {
        assertTrue(valid(null));
        assertTrue(valid(""));
        assertTrue(valid("   "));
    }
}
