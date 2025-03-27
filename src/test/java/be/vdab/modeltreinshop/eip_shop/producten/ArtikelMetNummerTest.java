package be.vdab.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Collections;
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
        final String fnummer;
        final String fmerk;
        final String fnaam;
        final String fomschrijving;

        if ("null".equals(nummer)) {
            fnummer = null;
        } else {
            fnummer = nummer;
        }
        if ("null".equals(merk)) {
            fmerk = null;
        } else {
            fmerk = merk;
        }
        if ("null".equals(naam)) {
            fnaam = null;
        } else {
            fnaam = naam;
        }
        if ("null".equals(omschrijving)) {
            fomschrijving = null;
        } else {
            fomschrijving = omschrijving;
        }

        if (expectException) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new ArtikelMetNummer(1L, fnummer, fmerk, fnaam, fomschrijving)
            );
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
        final String fnummer;
        final String fmerk;
        final String fnaam;
        final String fomschrijving;

        if (nummer != null && nummer.isBlank()) {
            fnummer = nummer.trim();
        } else {
            fnummer = nummer;
        }
        if (merk != null && merk.isBlank()) {
            fmerk = merk.trim();
        } else {
            fmerk = merk;
        }
        if (naam != null && naam.isBlank()) {
            fnaam = naam.trim();
        } else {
            fnaam = naam;
        }
        if (omschrijving != null && omschrijving.isBlank()) {
            fomschrijving = omschrijving.trim();
        } else {
            fomschrijving = omschrijving;
        }

        if (expectException) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new ArtikelMetNummer(1L, fnummer, fmerk, fnaam, fomschrijving)
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

