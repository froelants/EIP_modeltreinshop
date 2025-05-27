package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArtikelInVoorbestellingTest {
    private Artikel basisArtikel;
    private static final BigDecimal AANKOOPPRIJS = new BigDecimal("100.00");
    private static final BigDecimal WINSTMARGE = new BigDecimal("20.00");
    private static final BigDecimal VOORBESTELLING_PRIJS = new BigDecimal("150.00");
    private LocalDate voorbestellingTotDatum;
    private YearMonth leveringsKwartaal;
    private ArtikelInVoorbestelling artikel;

    @BeforeEach
    void beforeEach() {
        basisArtikel = new ArtikelMetNummer(1, "TEST001", "TestMerk",
                                            "TestNaam", "TestOmschrijving", false);
        voorbestellingTotDatum = LocalDate.now().plusMonths(1);
        leveringsKwartaal = YearMonth.now().plusMonths(3);
        artikel = new ArtikelInVoorbestelling(
                basisArtikel,// basisArtikel
                AANKOOPPRIJS,// aankoopprijs
                WINSTMARGE,// minimaleWinstmarge
                WinstmargeType.EURO,// winstmargeType
                VOORBESTELLING_PRIJS,// voorbestellingsprijs
                voorbestellingTotDatum,// leveringsdatum
                leveringsKwartaal   // eindeDoorlooptijd
                );
    }

    @ParameterizedTest
    @CsvSource({
            "100.00, 20.00, 150.00, false",           // Geldige waarden
            "100.00, 20.00, 90.00, true",            // Voorbestellingprijs te laag
            "0.00, 20.00, 150.00, true",             // Ongeldige aankoopprijs
            "-1.00, 20.00, 150.00, true",            // Negatieve aankoopprijs
            "100.00, -1.00, 150.00, true",           // Negatieve winstmarge
            "null, 20.00, 150.00, true",             // Null aankoopprijs
            "100.00, null, 150.00, true",            // Null winstmarge
            "100.00, 20.00, null, true"              // Null voorbestellingprijs
    })
    void constructor_ValidatieWaarden(String aankoopprijs,
                                      String winstmarge,
                                      String voorbestellingprijs,
                                      boolean verwachtException) {
        BigDecimal testAankoopprijs = "null".equals(aankoopprijs) ? null : new BigDecimal(aankoopprijs);
        BigDecimal testWinstmarge = "null".equals(winstmarge) ? null : new BigDecimal(winstmarge);
        BigDecimal testVoorbestellingprijs = "null".equals(voorbestellingprijs) ?
                null : new BigDecimal(voorbestellingprijs);

        if (verwachtException) {
            assertThrows(IllegalArgumentException.class, () ->
                    new ArtikelInVoorbestelling(
                            basisArtikel,// basisArtikel
                            testAankoopprijs, // aankoopprijs
                            testWinstmarge,// minimaleWinstmarge
                            WinstmargeType.EURO,// winstmargeType
                            testVoorbestellingprijs,// voorbestellingsprijs
                            voorbestellingTotDatum,// leveringsdatum
                            leveringsKwartaal// eindeDoorlooptijd
                    ));
        } else {
            ArtikelInVoorbestelling testArtikel = new ArtikelInVoorbestelling(
                    basisArtikel, testAankoopprijs,// basisArtikel
                    testWinstmarge,// aankoopprijs
                    WinstmargeType.EURO,// minimaleWinstmarge
                    testVoorbestellingprijs,// winstmargeType
                    voorbestellingTotDatum,// voorbestellingsprijs
                    leveringsKwartaal// leveringsdatum
            );
            assertTrue(testArtikel.isInVoorbestelling());
            assertEquals(testVoorbestellingprijs, testArtikel.getPrijsInVoorbestelling());
        }
    }

    @Test
    void getVerkoopprijs_TijdensVoorbestelling_GeeftVoorbestellingsPrijs() {
        assertEquals(VOORBESTELLING_PRIJS, artikel.getVerkoopprijs());
    }

    @Test
    void getVerkoopprijs_NaVoorbestelling_GeeftRegulierePrijs() {
        artikel.stopVoorbestelling();
        BigDecimal verwachteVerkoopprijs = WinstmargeType.EURO.getStrategy()
                .berekenVerkoopprijs(AANKOOPPRIJS, WINSTMARGE);
        assertEquals(verwachteVerkoopprijs, artikel.getVerkoopprijs());
    }

    @Test
    void artikelInVoorbestelling_KanNietInBackorder() {
        ArtikelInVoorbestelling artikelInVoorbestelling = new ArtikelInVoorbestelling(
                basisArtikel,
                new BigDecimal("100.00"),
                new BigDecimal("20.00"),
                WinstmargeType.EURO,
                new BigDecimal("150.00"),
                LocalDate.now().plusDays(30),
                YearMonth.now().plusMonths(3));

        ArtikelInVoorraad artikelInVoorraad = new ArtikelInVoorraad(
                artikelInVoorbestelling,
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
    @Test
    void getLeveringsKwartaalNummer_GeeftCorrectKwartaal() {
        YearMonth testKwartaal = YearMonth.of(2024, 4); // Q2
        ArtikelInVoorbestelling testArtikel = new ArtikelInVoorbestelling( basisArtikel,
                                                                           new BigDecimal("100.00"),
                                                                           new BigDecimal("20.00"),
                                                                           WinstmargeType.EURO,
                                                                           new BigDecimal("150.00"),
                                                                           LocalDate.now().plusDays(30),
                                                                           YearMonth.now().plusMonths(3));
        assertEquals(2, testArtikel.getLeveringsKwartaalNummer());
    }
    @Test
    void stopVoorbestelling_ZetAlleVoorbestellingsWaardenOpNull() {
        artikel.stopVoorbestelling();
        assertFalse(artikel.isInVoorbestelling());
        assertNull(artikel.getPrijsInVoorbestelling());
        assertNull(artikel.getInVoorbestellingTotDatum());
        assertNull(artikel.getLeveringsKwartaal());
    }

    @Test
    void startVoorbestelling_ValidatieVanNieuwePrijs() {
        artikel.stopVoorbestelling();
        assertThrows(IllegalArgumentException.class, () ->
                artikel.startVoorbestelling(
                        new BigDecimal("90.00"), // Prijs lager dan aankoopprijs
                        voorbestellingTotDatum,
                        leveringsKwartaal
                )
        );
    }

    @Test
    void delegatie_NaarBasisArtikel() {
        assertEquals(basisArtikel.getNummer(), artikel.getNummer());
        assertEquals(basisArtikel.getNaam(), artikel.getNaam());
        assertEquals(basisArtikel.getMerk(), artikel.getMerk());
    }

    @Test
    void onbekendeLeveringstijd_DefaultIsFalse() {
        assertFalse(artikel.heeftOnbekendeLeveringstijd());
    }

    @Test
    void onbekendeLeveringstijd_KanWordenIngesteld() {
        ArtikelInVoorbestelling artikelMetOnbekendeLeveringstijd = new ArtikelInVoorbestelling(
                basisArtikel,
                AANKOOPPRIJS,
                WINSTMARGE,
                WinstmargeType.EURO,
                VOORBESTELLING_PRIJS,
                voorbestellingTotDatum,
                leveringsKwartaal);

        assertTrue(artikelMetOnbekendeLeveringstijd.heeftOnbekendeLeveringstijd());
    }

    @Test
    void stopVoorbestelling_ResetsOnbekendeLeveringstijd() {
        ArtikelInVoorbestelling artikelMetOnbekendeLeveringstijd = new ArtikelInVoorbestelling(
                basisArtikel,
                AANKOOPPRIJS,
                WINSTMARGE,
                WinstmargeType.EURO,
                VOORBESTELLING_PRIJS,
                voorbestellingTotDatum,
                leveringsKwartaal);

        artikelMetOnbekendeLeveringstijd.stopVoorbestelling();
        assertFalse(artikelMetOnbekendeLeveringstijd.heeftOnbekendeLeveringstijd());
    }

    @Test
    void setOnbekendeLeveringstijd_WerktCorrect() {
        artikel.setOnbekendeLeveringstijd(true);
        assertTrue(artikel.heeftOnbekendeLeveringstijd());

        artikel.setOnbekendeLeveringstijd(false);
        assertFalse(artikel.heeftOnbekendeLeveringstijd());
    }

}
