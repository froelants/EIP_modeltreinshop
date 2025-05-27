package com.example.modeltreinshop.eip_shop.producten.model;

import com.example.modeltreinshop.eip_shop.producten.ArtikelInBackorder;
import com.example.modeltreinshop.eip_shop.producten.ArtikelInVoorbestelling;
import com.example.modeltreinshop.eip_shop.producten.ArtikelInVoorraad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArtikelInBackorderTest {
    private ArtikelInVoorraad basisArtikel;
    private LocalDate besteldatum;
    private ArtikelInBackorder artikelInBackorder;

    @BeforeEach
    void beforeEach() {
        Artikel artikelMetNummer = new ArtikelMetNummer(1, "TEST001", "TestMerk",
                                                        "TestNaam", "TestOmschrijving", false);
        basisArtikel = new ArtikelInVoorraad(artikelMetNummer, LocalDate.now(),
                                             new BigDecimal("100.00"), new BigDecimal("20.00"),
                                             WinstmargeType.EURO);
        besteldatum = LocalDate.now();
        artikelInBackorder = new ArtikelInBackorder(basisArtikel, besteldatum, 5, 7);
    }

    @Test
    void correcteConstructie() {
        assertDoesNotThrow(() -> new ArtikelInBackorder(basisArtikel, besteldatum, 5, 7));
    }

    @ParameterizedTest
    @CsvSource({
            "true, 5, 7, true",       // Eenmalig artikel
            "false, 0, 7, true",      // Ongeldig aantal
            "false, -1, 7, true",     // Negatief aantal
            "false, 5, 0, true",      // Ongeldige leveringstijd
            "false, 5, -1, true",     // Negatieve leveringstijd
            "false, 5, 7, false"      // Correcte waarden
    })
    void constructor_ValidatieParameters(boolean isEenmalig, int aantal,
                                         int leveringsDagen, boolean verwachtException) {
        Artikel basisArtikelTest = new ArtikelMetNummer(2, "TEST002", "TestMerk",
                                                        "TestNaam", "TestOmschrijving", isEenmalig);
        ArtikelInVoorraad artikelInVoorraadTest = new ArtikelInVoorraad(
                basisArtikelTest, LocalDate.now(),
                new BigDecimal("100.00"), new BigDecimal("20.00"),
                WinstmargeType.EURO);

        if (verwachtException) {
            assertThrows(IllegalArgumentException.class, () ->
                    new ArtikelInBackorder(artikelInVoorraadTest, besteldatum,
                                           aantal, leveringsDagen));
        } else {
            assertDoesNotThrow(() ->
                                       new ArtikelInBackorder(artikelInVoorraadTest, besteldatum,
                                                              aantal, leveringsDagen));
        }
    }

    @Test
    void berekenVerwachteLeveringsdatum_WerktCorrect() {
        LocalDate verwachteDatum = artikelInBackorder.berekenVerwachteLeveringsdatum();

        // Controleer of de verwachte datum correct is berekend
        assertEquals(besteldatum.plusDays(7), verwachteDatum);

        // Controleer of de laatste aankoopdatum is bijgewerkt
        assertEquals(verwachteDatum, basisArtikel.getLaatsteAankoopdatum());
    }

    @Test
    void getters_RetournerenCorrecteWaarden() {
        assertEquals(besteldatum, artikelInBackorder.getBesteldatum());
        assertEquals(5, artikelInBackorder.getAantalInBackorder());
        assertEquals(7, artikelInBackorder.getVerwachteLeveringstijdInDagen());
    }

    @Test
    void nullBesteldatum_GooitException() {
        assertThrows(IllegalArgumentException.class, () ->
                new ArtikelInBackorder(basisArtikel, null, 5, 7));
    }

    @Test
    void artikelInVoorbestelling_KanNietInBackorder() {
        // Eerst een ArtikelInVoorbestelling maken
        ArtikelInVoorbestelling artikelInVoorbestelling = new ArtikelInVoorbestelling(
                basisArtikel,
                new BigDecimal("100.00"),
                new BigDecimal("20.00"),
                WinstmargeType.EURO,
                new BigDecimal("150.00"),
                LocalDate.now().plusDays(30),
                YearMonth.now().plusMonths(3)
                );
        // Test of we een IllegalArgumentException krijgen als we proberen
        // een artikel in voorbestelling in backorder te zetten
        assertThrows(IllegalArgumentException.class, () ->
                new ArtikelInBackorder(basisArtikel, besteldatum, 5, 7));

    }

}
