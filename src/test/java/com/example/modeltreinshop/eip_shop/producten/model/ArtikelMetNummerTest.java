package com.example.modeltreinshop.eip_shop.producten;

import com.example.modeltreinshop.eip_shop.util.IllegalBlankArgumentException;
import com.example.modeltreinshop.eip_shop.util.IllegalNullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ArtikelMetNummerTest {
    private ArtikelMetNummer artikel;

    @BeforeEach
    void setUp() {
        artikel = new ArtikelMetNummer(1L, "123", "TestMerk", "TestNaam", "TestOmschrijving", false);
    }

    @ParameterizedTest
    @CsvSource({
            "'123', 'Merk', 'Naam', 'Omschrijving', false, false",   // Geldig
            "null, 'Merk', 'Naam', 'Omschrijving', false, true",     // nummer is null
            "'123', null, 'Naam', 'Omschrijving', false, true",      // merk is null
            "'123', 'Merk', null, 'Omschrijving', false, true",      // naam is null
            "'123', 'Merk', 'Naam', null, false, true",              // omschrijving is null
            "null, null, null, null, false, true",                   // alles is null
            "'123', 'Merk', 'Naam', 'Omschrijving', true, false",   // Geldig met eenmaligArtikel true
    })
    void constructor_ShouldHandleNullValuesCorrectly(String nummer, String merk, String naam,
                                                     String omschrijving, boolean eenmaligArtikel, boolean expectException) {
        final String finalNummer = "null".equals(nummer) ? null : nummer;
        final String finalMerk = "null".equals(merk) ? null : merk;
        final String finalNaam = "null".equals(naam) ? null : naam;
        final String finalOmschrijving = "null".equals(omschrijving) ? null : omschrijving;

        if (expectException) {
            IllegalNullArgumentException exception = assertThrows(IllegalNullArgumentException.class,
                                                                  () -> new ArtikelMetNummer(1L, finalNummer, finalMerk, finalNaam, finalOmschrijving, eenmaligArtikel));
            assertTrue(exception.getMessage().contains("mag niet null zijn"));
        } else {
            ArtikelMetNummer testArtikel = new ArtikelMetNummer(1L, nummer, merk, naam, omschrijving, eenmaligArtikel);
            assertNotNull(testArtikel);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "'123', 'Merk', 'Naam', 'Omschrijving', false, false",   // Geldig
            "'', 'Merk', 'Naam', 'Omschrijving', false, true",      // nummer is leeg
            "'123', '', 'Naam', 'Omschrijving', false, true",       // merk is leeg
            "'123', 'Merk', '', 'Omschrijving', false, true",       // naam is leeg
            "'123', 'Merk', 'Naam', '', false, true",               // omschrijving is leeg
            "' ', 'Merk', 'Naam', 'Omschrijving', false, true",     // nummer is spatie
            "'123', ' ', 'Naam', 'Omschrijving', false, true",      // merk is spatie
    })
    void constructor_ShouldHandleBlankValuesCorrectly(String nummer, String merk, String naam,
                                                      String omschrijving, boolean eenmaligArtikel, boolean expectException) {
        if (expectException) {
            assertThrows(IllegalBlankArgumentException.class,
                         () -> new ArtikelMetNummer(1L, nummer, merk, naam, omschrijving, eenmaligArtikel));
        } else {
            ArtikelMetNummer testArtikel = new ArtikelMetNummer(1L, nummer, merk, naam, omschrijving, eenmaligArtikel);
            assertNotNull(testArtikel);
        }
    }

    @Test
    void setters_MetGeldigeWaarden_WerkenCorrect() {
        artikel.setNaam("NieuweNaam");
        assertEquals("NieuweNaam", artikel.getNaam());

        artikel.setMerk("NieuwMerk");
        assertEquals("NieuwMerk", artikel.getMerk());

        artikel.setOmschrijving("NieuweOmschrijving");
        assertEquals("NieuweOmschrijving", artikel.getOmschrijving());
    }

    @Test
    void setters_MetOngeldigeWaarden_GooienExceptions() {
        assertThrows(IllegalArgumentException.class, () -> artikel.setNaam(null));
        assertThrows(IllegalArgumentException.class, () -> artikel.setNaam("  "));
        assertThrows(IllegalArgumentException.class, () -> artikel.setMerk(null));
        assertThrows(IllegalArgumentException.class, () -> artikel.setMerk("  "));
        assertThrows(IllegalArgumentException.class, () -> artikel.setOmschrijving(null));
        assertThrows(IllegalArgumentException.class, () -> artikel.setOmschrijving("  "));
    }

    @Test
    void afbeeldingen_Beheer() {
        // Test toevoegen
        assertTrue(artikel.addAfbeelding("test.jpg"));
        assertEquals(1, artikel.getAfbeeldingen().size());

        // Test verwijderen
        assertTrue(artikel.removeAfbeelding("test.jpg"));
        assertTrue(artikel.getAfbeeldingen().isEmpty());

        // Test ongeldige afbeeldingen
        assertThrows(IllegalArgumentException.class, () -> artikel.addAfbeelding(null));
        assertThrows(IllegalArgumentException.class, () -> artikel.addAfbeelding("  "));
        assertThrows(IllegalArgumentException.class, () -> artikel.removeAfbeelding("nietbestaat.jpg"));
    }

    @Test
    void getAfbeeldingen_IsOnmodifiable() {
        artikel.addAfbeelding("test.jpg");
        assertThrows(UnsupportedOperationException.class,
                     () -> artikel.getAfbeeldingen().add("nieuw.jpg"));
    }

    @Test
    void equals_EnHashCode() {
        ArtikelMetNummer zelfdeArtikel = new ArtikelMetNummer(1L, "123", "AnderId",
                                                              "AndereNaam", "AndereOmschrijving", true);
        ArtikelMetNummer anderArtikel = new ArtikelMetNummer(2L, "456", "TestMerk",
                                                             "TestNaam", "TestOmschrijving", false);

        assertEquals(artikel, zelfdeArtikel);
        assertEquals(artikel.hashCode(), zelfdeArtikel.hashCode());
        assertNotEquals(artikel, anderArtikel);
    }

    @Test
    void toString_BevatAlleRelevanteInformatie() {
        String toString = artikel.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nummer='123'"));
        assertTrue(toString.contains("naam='TestNaam'"));
        assertTrue(toString.contains("merk='TestMerk'"));
        assertTrue(toString.contains("omschrijving='TestOmschrijving'"));
    }

    @ParameterizedTest
    @CsvSource({
            "NieuweNaam, null, null, true",
            "null, NieuwMerk, null, true",
            "null, null, NieuweOmschrijving, true",
            "'  ', null, null, false",
            "null, '  ', null, false",
            "null, null, '  ', false"
    })
    void setters_ValidatieWaarden(String naam, String merk, String omschrijving,
                                  boolean geldigeWaarde) {
        if (geldigeWaarde) {
            if (naam != null) {
                artikel.setNaam(naam);
                assertEquals(naam, artikel.getNaam());
            }
            if (merk != null) {
                artikel.setMerk(merk);
                assertEquals(merk, artikel.getMerk());
            }
            if (omschrijving != null) {
                artikel.setOmschrijving(omschrijving);
                assertEquals(omschrijving, artikel.getOmschrijving());
            }
        } else {
            if (naam != null) {
                assertThrows(IllegalArgumentException.class,
                             () -> artikel.setNaam(naam));
            }
            if (merk != null) {
                assertThrows(IllegalArgumentException.class,
                             () -> artikel.setMerk(merk));
            }
            if (omschrijving != null) {
                assertThrows(IllegalArgumentException.class,
                             () -> artikel.setOmschrijving(omschrijving));
            }
        }
    }

    @ParameterizedTest
    @CsvSource({
            "test1.jpg, true, true",     // Nieuwe afbeelding
            "null, true, false",         // Null afbeelding
            "'  ', true, false",         // Lege afbeelding
            "test1.jpg, false, true",    // Bestaande afbeelding verwijderen
            "onbekend.jpg, false, false" // Niet-bestaande afbeelding verwijderen
    })
    void afbeeldingen_BeheerMetVerschillendeWaarden(String afbeelding,
                                                    boolean isToevoegen, boolean verwachtSucces) {
        String testAfbeelding = "null".equals(afbeelding) ? null : afbeelding;

        if (isToevoegen) {
            if (!verwachtSucces) {
                assertThrows(IllegalArgumentException.class,
                             () -> artikel.addAfbeelding(testAfbeelding));
            } else {
                assertTrue(artikel.addAfbeelding(testAfbeelding));
                assertTrue(artikel.getAfbeeldingen().contains(testAfbeelding));
            }
        } else {
            if (verwachtSucces) {
                artikel.addAfbeelding(testAfbeelding);
            }
            if (!verwachtSucces) {
                assertThrows(IllegalArgumentException.class,
                             () -> artikel.removeAfbeelding(testAfbeelding));
            } else {
                assertTrue(artikel.removeAfbeelding(testAfbeelding));
                assertFalse(artikel.getAfbeeldingen().contains(testAfbeelding));
            }
        }
    }

}
