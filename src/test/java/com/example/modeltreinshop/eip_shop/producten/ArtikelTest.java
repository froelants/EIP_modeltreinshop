package com.example.modeltreinshop.eip_shop.producten;

package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class ArtikelTest {

    // Test implementation of abstract class
    private static class TestArtikel extends Artikel {
        public TestArtikel(String artikelnummer, String naam, String merk,
                           String omschrijving, boolean gratisArtikel,
                           BigDecimal aankoopprijs, BigDecimal winstmarge,
                           WinstmargeType winstmargeType, BigDecimal verkoopprijs,
                           List<String> afbeeldingen) {
            super(artikelnummer, naam, merk, omschrijving, gratisArtikel,
                  aankoopprijs, winstmarge, winstmargeType, verkoopprijs, afbeeldingen);
        }
    }

    private static Stream<Arguments> validArtikelParameters() {
        return Stream.of(
                // Normal artikel
                Arguments.of(
                        "ART001", "Test Artikel", "Test Merk", "Omschrijving",
                        false, new BigDecimal("10.00"), new BigDecimal("1.5"),
                        WinstmargeType.PERCENTAGE, new BigDecimal("25.00"),
                        List.of("https://example.com/img1.jpg")
                ),
                // Gratis artikel
                Arguments.of(
                        "ART002", "Gratis Item", "Test Merk", "Gratis omschrijving",
                        true, BigDecimal.ZERO, BigDecimal.ZERO,
                        WinstmargeType.PERCENTAGE, BigDecimal.ZERO,
                        List.of("https://example.com/img2.jpg")
                )
        );
    }

    @ParameterizedTest
    @MethodSource("validArtikelParameters")
    void testValidConstructor(String artikelnummer, String naam, String merk,
                              String omschrijving, boolean gratisArtikel,
                              BigDecimal aankoopprijs, BigDecimal winstmarge,
                              WinstmargeType winstmargeType, BigDecimal verkoopprijs,
                              List<String> afbeeldingen) {
        TestArtikel artikel = new TestArtikel(
                artikelnummer, naam, merk, omschrijving, gratisArtikel,
                aankoopprijs, winstmarge, winstmargeType, verkoopprijs, afbeeldingen
        );

        assertEquals(artikelnummer, artikel.getArtikelnummer());
        assertEquals(naam, artikel.getNaam());
        assertEquals(merk, artikel.getMerk());
        assertEquals(omschrijving, artikel.getOmschrijving());
        assertEquals(gratisArtikel, artikel.isGratisArtikel());
        assertEquals(verkoopprijs, artikel.getVerkoopprijs());
        assertEquals(afbeeldingen, artikel.getAfbeeldingen());
    }

    @Test
    void testNullParameters() {
        assertThrows(IllegalNullArgumentException.class, () -> new TestArtikel(
                null, "naam", "merk", "omschrijving", false,
                new BigDecimal("10.00"), new BigDecimal("1.5"),
                WinstmargeType.PERCENTAGE, new BigDecimal("25.00"),
                List.of("https://example.com/img1.jpg")
        ));
    }

    @Test
    void testEmptyAfbeeldingen() {
        assertThrows(IllegalArgumentException.class, () -> new TestArtikel(
                "ART001", "naam", "merk", "omschrijving", false,
                new BigDecimal("10.00"), new BigDecimal("1.5"),
                WinstmargeType.PERCENTAGE, new BigDecimal("25.00"),
                List.of()
        ));
    }

    @Test
    void testEqualsAndHashCode() {
        TestArtikel artikel1 = new TestArtikel(
                "ART001", "Test", "Merk", "Omschrijving", false,
                new BigDecimal("10.00"), new BigDecimal("1.5"),
                WinstmargeType.PERCENTAGE, new BigDecimal("25.00"),
                List.of("https://example.com/img1.jpg")
        );

        TestArtikel artikel2 = new TestArtikel(
                "ART001", "Different", "Different", "Different", true,
                BigDecimal.ZERO, BigDecimal.ZERO,
                WinstmargeType.PERCENTAGE, BigDecimal.ZERO,
                List.of("https://example.com/img2.jpg")
        );

        TestArtikel artikel3 = new TestArtikel(
                "ART002", "Test", "Merk", "Omschrijving", false,
                new BigDecimal("10.00"), new BigDecimal("1.5"),
                WinstmargeType.PERCENTAGE, new BigDecimal("25.00"),
                List.of("https://example.com/img1.jpg")
        );

        assertEquals(artikel1, artikel2);
        assertNotEquals(artikel1, artikel3);
        assertEquals(artikel1.hashCode(), artikel2.hashCode());
        assertNotEquals(artikel1.hashCode(), artikel3.hashCode());
    }

    @Test
    void testToString() {
        TestArtikel artikel = new TestArtikel(
                "ART001", "Test", "Merk", "Omschrijving", false,
                new BigDecimal("10.00"), new BigDecimal("1.5"),
                WinstmargeType.PERCENTAGE, new BigDecimal("25.00"),
                List.of("https://example.com/img1.jpg")
        );

        String expected = "TestArtikel [ART001] Merk Test - €25.00 - Omschrijving - 1 afbeelding(en)\n" +
                          "Afbeeldingen:\n" +
                          "  https://example.com/img1.jpg\n";
        assertEquals(expected, artikel.toString());
    }

    @Test
    void testKorting() {
        TestArtikel artikel = new TestArtikel(
                "ART001", "Test", "Merk", "Omschrijving", false,
                new BigDecimal("10.00"), new BigDecimal("1.5"),
                WinstmargeType.PERCENTAGE, new BigDecimal("25.00"),
                List.of("https://example.com/img1.jpg")
        );

        artikel.setKorting(new BigDecimal("20.00"), LocalDate.now().plusDays(1));
        assertTrue(artikel.isInKorting());
        assertEquals(new BigDecimal("20.00"), artikel.getVerkoopprijs());

        artikel.stopKorting();
        assertFalse(artikel.isInKorting());
        assertEquals(new BigDecimal("25.00"), artikel.getVerkoopprijs());
    }

    @Test
    void testAfbeeldingen() {
        TestArtikel artikel = new TestArtikel(
                "ART001", "Test", "Merk", "Omschrijving", false,
                new BigDecimal("10.00"), new BigDecimal("1.5"),
                WinstmargeType.PERCENTAGE, new BigDecimal("25.00"),
                List.of("https://example.com/img1.jpg")
        );

        assertTrue(artikel.addAfbeelding("https://example.com/img2.jpg"));
        assertEquals(2, artikel.getAfbeeldingen().size());
        assertTrue(artikel.removeAfbeelding("https://example.com/img2.jpg"));
        assertEquals(1, artikel.getAfbeeldingen().size());

        assertThrows(IllegalArgumentException.class, () -> artikel.addAfbeelding(null));
        assertThrows(IllegalArgumentException.class, () -> artikel.addAfbeelding(""));
        assertThrows(IllegalArgumentException.class, () -> artikel.removeAfbeelding("nonexistent"));
    }
}