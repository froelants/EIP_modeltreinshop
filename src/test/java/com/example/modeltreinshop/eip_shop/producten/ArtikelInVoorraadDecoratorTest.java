package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class ArtikelInVoorraadTest {
    private Artikel basisArtikel;
    private LocalDate datum;
    private ArtikelInVoorraad artikel;

    @BeforeEach
    void beforeEach() {
        basisArtikel = new ArtikelMetNummer(1, "TEST001", "TestMerk",
                                            "TestNaam", "TestOmschrijving", false);
        datum = LocalDate.now();
        artikel = new ArtikelInVoorraad(basisArtikel, datum,
                                        new BigDecimal("100.00"), new BigDecimal("20.00"),
                                        WinstmargeType.EURO);
    }

    @Test
    void artikelInVoorbestelling_KanNietInBackorder() {
        ArtikelInVoorbestellingDecorator artikelInVoorbestellingDecorator = new ArtikelInVoorbestellingDecorator(
                basisArtikel,            // basisArtikel
                new BigDecimal("100.00"), // aankoopprijs
                new BigDecimal("20.00"),  // minimaleWinstmarge
                WinstmargeType.EURO,      // winstmargeType
                new BigDecimal("150.00"), // voorbestellingsprijs
                LocalDate.now().plusDays(30), // voorTeBestellenTotDatum
                YearMonth.now().plusMonths(3) // leveringskwartaal
        );
        ArtikelInVoorraad artikelInVoorraad = new ArtikelInVoorraad(
                artikelInVoorbestellingDecorator,
                LocalDate.now(),
                new BigDecimal("100.00"),
                new BigDecimal("20.00"),
                WinstmargeType.EURO
        );
        LocalDate bestelDatum = LocalDate.now();
        assertThrows(IllegalArgumentException.class, () ->
                             new ArtikelInBackorder(artikelInVoorraad, bestelDatum, 5, 7),
                     "Artikel in voorbestelling kan niet in backorder worden geplaatst");
    }

    @ParameterizedTest
    @CsvSource({
            "100.00, 20.00, false",    // Geldige waarden
            "-1.00, 20.00, true",      // Negatieve aankoopprijs
            "100.00, -1.00, true",     // Negatieve winstmarge
            "0.00, 0.00, false",       // Beide nul (geldig)
            "null, 20.00, true",       // Null aankoopprijs
            "100.00, null, true"       // Null winstmarge
    })
    void setters_ValidatieWaarden(String nieuwePrijs, String nieuweWinstmarge,
                                  boolean verwachtException) {
        if (verwachtException) {
            if ("null".equals(nieuwePrijs)) {
                assertThrows(IllegalArgumentException.class, () ->
                        artikel.getPrijsComponent().setAankoopprijs(null));
            } else if ("null".equals(nieuweWinstmarge)) {
                assertThrows(IllegalArgumentException.class, () ->
                        artikel.getPrijsComponent().setMinimaleWinstmarge(null));
            } else {
                BigDecimal prijs = new BigDecimal(nieuwePrijs);
                BigDecimal marge = new BigDecimal(nieuweWinstmarge);

                if (prijs.compareTo(BigDecimal.ZERO) < 0) {
                    assertThrows(IllegalArgumentException.class, () ->
                            artikel.getPrijsComponent().setAankoopprijs(prijs));
                } else {
                    assertThrows(IllegalArgumentException.class, () ->
                            artikel.getPrijsComponent().setMinimaleWinstmarge(marge));
                }
            }
        } else {
            assertDoesNotThrow(() -> {
                artikel.getPrijsComponent().setAankoopprijs(new BigDecimal(nieuwePrijs));
                artikel.getPrijsComponent().setMinimaleWinstmarge(new BigDecimal(nieuweWinstmarge));
            });
        }
    }

    @Test
    void testDelegatie() {
        assertEquals(basisArtikel.getNummer(), artikel.getNummer());
        assertEquals(basisArtikel.getNaam(), artikel.getNaam());
    }

    @Test
    void testSettersMetNullDatum() {
        assertThrows(IllegalArgumentException.class,
                     () -> artikel.setLaatsteAankoopdatum(null));
    }

    @Test
    void berekenVerkoopprijs_UpdateBijWijzigingen() {
        // Test update na wijziging aankoopprijs
        artikel.getPrijsComponent().setAankoopprijs(new BigDecimal("150.00"));
        assertEquals(new BigDecimal("170.00"), artikel.getVerkoopprijs());

        // Test update na wijziging winstmarge
        artikel.getPrijsComponent().setMinimaleWinstmarge(new BigDecimal("30.00"));
        assertEquals(new BigDecimal("180.00"), artikel.getVerkoopprijs());
    }
}
