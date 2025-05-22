package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CourantArtikelTest {
    private Artikel basisArtikel;
    private LocalDate datum;
    private CourantArtikel artikel;
    private static final BigDecimal AANKOOPPRIJS = new BigDecimal("100.00");
    private static final BigDecimal WINSTMARGE = new BigDecimal("20.00");

    @BeforeEach
    void beforeEach() {
        basisArtikel = new ArtikelMetNummer(1, "TEST001", "TestMerk",
                "TestNaam", "TestOmschrijving", false);
        datum = LocalDate.now();
        artikel = new CourantArtikel(
                basisArtikel, datum, AANKOOPPRIJS, WINSTMARGE,
                WinstmargeType.EURO, 5, 10, 2);
    }

    @Test
    void getVerkoopprijs_ZonderKorting_GeeftNormaleVerkoopprijs() {
        assertEquals(new BigDecimal("120.00"), artikel.getVerkoopprijs());
    }

    @Test
    void getVerkoopprijs_MetKorting_GeeftKortingsprijs() {
        artikel.startKorting(LocalDate.now().plusDays(1), 5, new BigDecimal("110.00"));
        assertEquals(new BigDecimal("110.00"), artikel.getVerkoopprijs());
    }

    @ParameterizedTest
    @CsvSource({
        "90.00, true",    // Onder minimale prijs (aankoopprijs + winstmarge)
        "119.99, false",  // Net onder normale prijs
        "120.00, true",   // Gelijk aan normale prijs
        "121.00, true"    // Boven normale prijs
    })
    void startKorting_ValidatieKortingsprijs(String prijsInKorting, boolean verwachtException) {
        if (verwachtException) {
            assertThrows(IllegalArgumentException.class, () ->
                artikel.startKorting(LocalDate.now().plusDays(1), 5, 
                    new BigDecimal(prijsInKorting))
            );
        } else {
            assertDoesNotThrow(() ->
                artikel.startKorting(LocalDate.now().plusDays(1), 5, 
                    new BigDecimal(prijsInKorting))
            );
        }
    }

    @Test
    void startKorting_MetNegatieveRest_GooitException() {
        assertThrows(IllegalArgumentException.class, () ->
            artikel.startKorting(LocalDate.now().plusDays(1), -1, 
                new BigDecimal("110.00"))
        );
    }

    @Test
    void verminderKortingRest_UpdatetRest() {
        artikel.startKorting(LocalDate.now().plusDays(1), 5, new BigDecimal("110.00"));
        artikel.verminderKortingRest(2);
        
        assertEquals(3, artikel.getKortingTotRest());
        assertEquals(new BigDecimal("110.00"), artikel.getVerkoopprijs());
    }

    @Test
    void verminderKortingRest_TotNul_StoptKorting() {
        artikel.startKorting(LocalDate.now().plusDays(1), 2, new BigDecimal("110.00"));
        artikel.verminderKortingRest(2);
        
        assertFalse(artikel.isInKorting());
        assertEquals(new BigDecimal("120.00"), artikel.getVerkoopprijs());
    }

    @Test
    void setLaatsteAankoopprijs_StoptKortingBijOngeldigeKortingsprijs() {
        artikel.startKorting(LocalDate.now().plusDays(1), 5, new BigDecimal("110.00"));
        artikel.getPrijsComponent().setAankoopprijs(new BigDecimal("100.00"));
        
        // Nieuwe minimale prijs wordt 120.00, dus korting van 110.00 is niet meer geldig
        assertFalse(artikel.isInKorting());
        assertEquals(new BigDecimal("120.00"), artikel.getVerkoopprijs());
    }

    @Test
    void stopKorting_HersteltNormaleVerkoopprijs() {
        artikel.startKorting(LocalDate.now().plusDays(1), 5, new BigDecimal("110.00"));
        artikel.stopKorting();
        
        assertFalse(artikel.isInKorting());
        assertEquals(new BigDecimal("120.00"), artikel.getVerkoopprijs());
    }
}