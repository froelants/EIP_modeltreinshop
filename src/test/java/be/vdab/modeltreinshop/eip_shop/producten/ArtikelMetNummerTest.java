package be.vdab.modeltreinshop.eip_shop.producten;

import be.vdab.modeltreinshop.eip_shop.util.IllegalBlankArgumentException;
import be.vdab.modeltreinshop.eip_shop.util.IllegalNullArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArtikelMetNummerTest {

    @ParameterizedTest
    @CsvSource({
            // Test combinaties waarbij null-waardes een exception moeten gooien
            "'123', 'Merk', 'Naam', 'Omschrijving', false",   // Geldig
            "null, 'Merk', 'Naam', 'Omschrijving', true",     // nummer is null
            "'123', null, 'Naam', 'Omschrijving', true",      // merk is null
            "'123', 'Merk', null, 'Omschrijving', true",      // naam is null
            "'123', 'Merk', 'Naam', null, true",              // omschrijving is null
            "null, null, null, null, true",                   // alles is null
    })
    void constructor_ShouldHandleNullValuesCorrectly(String nummer, String merk, String naam, String omschrijving, boolean expectException) {
        final String finalNummer;
        final String finalMerk;
        final String finalNaam;
        final String finalOmschrijving;

        if ("null".equals(nummer)) {
            finalNummer = null;
        } else {
            finalNummer = nummer;
        }
        if ("null".equals(merk)) {
            finalMerk = null;
        } else {
            finalMerk = merk;
        }
        if ("null".equals(naam)) {
            finalNaam = null;
        } else {
            finalNaam = naam;
        }
        if ("null".equals(omschrijving)) {
            finalOmschrijving = null;
        } else {
            finalOmschrijving = omschrijving;
        }

        if (expectException) {
            IllegalNullArgumentException exception = assertThrows(IllegalNullArgumentException.class, () -> new ArtikelMetNummer(1L, finalNummer, finalMerk, finalNaam, finalOmschrijving) );
            assertTrue(exception.getMessage().contains("mag niet null zijn"));
        } else {
            ArtikelMetNummer artikel = new ArtikelMetNummer(1L, nummer, merk, naam, omschrijving);
            assertNotNull(artikel);
        }
    }

    @ParameterizedTest
    @CsvSource({
            // Test combinaties waarbij lege strings een exception moeten gooien
            "'123', 'Merk', 'Naam', 'Omschrijving', false",   // Geldig
            "'', 'Merk', 'Naam', 'Omschrijving', true",      // nummer is leeg
            "'123', '', 'Naam', 'Omschrijving', true",       // merk is leeg
            "'123', 'Merk', '', 'Omschrijving', true",       // naam is leeg
            "'123', 'Merk', 'Naam', '', true",               // omschrijving is leeg
            "'', '', '', '', true",                       // alles is leeg
            "' ', 'Merk', 'Naam', 'Omschrijving', true",      // nummer is leeg
            "'123', ' ', 'Naam', 'Omschrijving', true",       // merk is leeg
            "'123', 'Merk', ' ', 'Omschrijving', true",       // naam is leeg
            "'123', 'Merk', 'Naam', ' ', true",               // omschrijving is leeg
            "' ', ' ', ' ', ' ', true",                       // alles is leeg

    })
    void constructor_ShouldHandleBlankValuesCorrectly(String nummer, String merk, String naam, String omschrijving, boolean expectException) {
        final String finalNummer;
        final String finalMerk;
        final String finalNaam;
        final String finalOmschrijving;

        if (nummer != null && nummer.isBlank()) {
            finalNummer = nummer.trim();
        } else {
            finalNummer = nummer;
        }
        if (merk != null && merk.isBlank()) {
            finalMerk = merk.trim();
        } else {
            finalMerk = merk;
        }
        if (naam != null && naam.isBlank()) {
            finalNaam = naam.trim();
        } else {
            finalNaam = naam;
        }
        if (omschrijving != null && omschrijving.isBlank()) {
            finalOmschrijving = omschrijving.trim();
        } else {
            finalOmschrijving = omschrijving;
        }

        if (expectException) {
            IllegalBlankArgumentException exception = assertThrows(IllegalBlankArgumentException.class, () ->
                    new ArtikelMetNummer(1L, finalNummer, merk, finalNaam, finalOmschrijving)
            );
            assertTrue(exception.getMessage().contains("mag niet leeg zijn"));
        } else {
            ArtikelMetNummer artikel = new ArtikelMetNummer(1L, nummer, merk, naam, omschrijving);
            assertNotNull(artikel);
        }
    }

    @Test
    void constructor_ShouldThrowIllegalArgumentException_WhenNullAndBlankParametersCombined() {
        // Test specifieke combinaties van null- en lege waarden
        List<String> exceptions = new ArrayList<>();

        try {
            new ArtikelMetNummer(1L, null, "Merk", "Naam", "Omschrijving");
        } catch (IllegalArgumentException e) {
            exceptions.add(e.getMessage());
        }

        try {
            new ArtikelMetNummer(1L, "123", " ", "Naam", "Omschrijving");
        } catch (IllegalArgumentException e) {
            exceptions.add(e.getMessage());
        }

        try {
            new ArtikelMetNummer(1L, "123", "Merk", null, "Omschrijving");
        } catch (IllegalArgumentException e) {
            exceptions.add(e.getMessage());
        }

        // Controleer dat de juiste uitzonderingen gegooid zijn
        assertFalse(exceptions.isEmpty());
        assertTrue(exceptions.stream().anyMatch(msg -> msg.contains("nummer")));
        assertTrue(exceptions.stream().anyMatch(msg -> msg.contains("merk")));
        assertTrue(exceptions.stream().anyMatch(msg -> msg.contains("naam")));
    }

    @Test
    void validConstructor_ShouldCreateObject() {
        ArtikelMetNummer artikel = new ArtikelMetNummer(1L, "123", "Merk", "Naam", "Omschrijving");
        assertNotNull(artikel);
        assertEquals(1L, artikel.getId());
        assertEquals("123", artikel.getNummer());
        assertEquals("Merk", artikel.getMerk());
        assertEquals("Naam", artikel.getNaam());
        assertEquals("Omschrijving", artikel.getOmschrijving());
    }
}

