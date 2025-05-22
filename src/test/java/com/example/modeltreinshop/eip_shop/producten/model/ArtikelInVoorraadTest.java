package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
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

    @ParameterizedTest
    @CsvSource({
            "100.00, 20.00, EURO, 120.00, false",      // Normale euro berekening
            "100.00, 20.00, PERCENTAGE, 120.00, false", // Normale percentage berekening
            "0.00, 0.00, EURO, 0.00, false",           // Nul waarden
            "100.00, 0.00, EURO, '', true",            // Ongeldige winstmarge bij positieve prijs
            "0.00, 20.00, EURO, '', true"              // Ongeldige winstmarge bij nul prijs
    })
    void constructor_ValidatieEnBerekening(String aankoopprijs, String winstmarge,
                                           WinstmargeType type, String verwachteVerkoopprijs, boolean verwachtException) {
        if (verwachtException) {
            assertThrows(IllegalArgumentException.class, () ->
                    new ArtikelInVoorraad(
                            basisArtikel,
                            datum,
                            new BigDecimal(aankoopprijs),
                            new BigDecimal(winstmarge),
                            type
                    )
            );
        } else {
            ArtikelInVoorraad testArtikel = new ArtikelInVoorraad(
                    basisArtikel,
                    datum,
                    new BigDecimal(aankoopprijs),
                    new BigDecimal(winstmarge),
                    type
            );
            assertEquals(new BigDecimal(verwachteVerkoopprijs), testArtikel.getVerkoopprijs());
        }
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
        assertEquals(basisArtikel.getId(), artikel.getId());
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

