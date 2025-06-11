package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ArtikelInVoorbestelling Tests")
class ArtikelInVoorbestellingTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/ArtikelInVoorbestelling_ValidArticles.csv", numLinesToSkip = 1)
    @DisplayName("Should create ArtikelInVoorbestelling with valid data")
    void shouldCreateArtikelInVoorbestelling(
            String artikelnummer,
            String naam,
            String merk,
            String omschrijving,
            boolean gratisArtikel,
            BigDecimal aankoopprijs,
            BigDecimal winstmarge,
            WinstmargeType winstmargeType,
            BigDecimal verkoopprijs,
            String afbeeldingen,
            LocalDate leverDatum,
            int minimumAantal,
            int voorbestellingen) {

        List<String> afbeeldingenList = List.of(afbeeldingen.split(";"));

        ArtikelInVoorbestelling artikel = new ArtikelInVoorbestelling(
                artikelnummer, naam, merk, omschrijving, gratisArtikel,
                aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
                afbeeldingenList, leverDatum, minimumAantal);

        for (int i = 0; i < voorbestellingen; i++) {
            artikel.voegVoorbestellingToe();
        }

        assertThat(artikel.getArtikelnummer()).isEqualTo(artikelnummer);
        assertThat(artikel.getNaam()).isEqualTo(naam);
        assertThat(artikel.getMerk()).isEqualTo(merk);
        assertThat(artikel.getLeverDatum()).isEqualTo(leverDatum);
        assertThat(artikel.getMinimumAantal()).isEqualTo(minimumAantal);
        assertThat(artikel.getAantalVoorbestellingen()).isEqualTo(voorbestellingen);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ArtikelInVoorbestelling_InvalidDates.csv", numLinesToSkip = 1)
    @DisplayName("Should throw exception for past delivery dates")
    void shouldThrowExceptionForPastDeliveryDates(
            String artikelnummer,
            String naam,
            String merk,
            String omschrijving,
            boolean gratisArtikel,
            BigDecimal aankoopprijs,
            BigDecimal winstmarge,
            WinstmargeType winstmargeType,
            BigDecimal verkoopprijs,
            String afbeeldingen,
            LocalDate leverDatum,
            int minimumAantal,
            int voorbestellingen) {

        List<String> afbeeldingenList = List.of(afbeeldingen.split(";"));

        assertThatThrownBy(() -> new ArtikelInVoorbestelling(
                artikelnummer, naam, merk, omschrijving, gratisArtikel,
                aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
                afbeeldingenList, leverDatum, minimumAantal))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Leverdatum moet in de toekomst liggen");
    }

    @Test
    @DisplayName("Should throw exception when delivery date is null")
    void shouldThrowExceptionWhenDeliveryDateIsNull() {
        assertThatThrownBy(() -> new ArtikelInVoorbestelling(
                "43660", "ESU V60", "ESU", "Test lok", false,
                new BigDecimal("449.99"), new BigDecimal("55.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("697.49"),
                List.of("43660.jpg"), null, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Leverdatum mag niet null zijn");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    @DisplayName("Should throw exception for invalid minimum amounts")
    void shouldThrowExceptionForInvalidMinimumAmounts(int invalidAmount) {
        assertThatThrownBy(() -> new ArtikelInVoorbestelling(
                "43660", "ESU V60", "ESU", "Test lok", false,
                new BigDecimal("449.99"), new BigDecimal("55.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("697.49"),
                List.of("43660.jpg"), LocalDate.now().plusMonths(1), invalidAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Minimum aantal moet positief zijn");
    }

    @Test
    @DisplayName("Should handle voorbestelling management correctly")
    void shouldHandleVoorbestellingManagementCorrectly() {
        ArtikelInVoorbestelling artikel = createTestArtikel();

        // Add voorbestellingen
        for (int i = 0; i < 5; i++) {
            artikel.voegVoorbestellingToe();
        }
        assertThat(artikel.getAantalVoorbestellingen()).isEqualTo(5);

        // Remove voorbestellingen
        for (int i = 0; i < 3; i++) {
            artikel.verwijderVoorbestelling();
        }
        assertThat(artikel.getAantalVoorbestellingen()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should throw exception when removing non-existent voorbestelling")
    void shouldThrowExceptionWhenRemovingNonExistentVoorbestelling() {
        ArtikelInVoorbestelling artikel = createTestArtikel();

        assertThatThrownBy(artikel::verwijderVoorbestelling)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Geen voorbestellingen om te verwijderen");
    }

    @Test
    @DisplayName("Should calculate remaining days correctly")
    void shouldCalculateRemainingDaysCorrectly() {
        LocalDate leverDatum = LocalDate.now().plusDays(30);
        ArtikelInVoorbestelling artikel = createTestArtikel(leverDatum);

        long expectedDays = LocalDate.now().until(leverDatum).getDays();
        assertThat(artikel.getResterendeDagen()).isEqualTo(expectedDays);
    }

    @Test
    @DisplayName("Should handle toString correctly")
    void shouldHandleToStringCorrectly() {
        ArtikelInVoorbestelling artikel = createTestArtikel();
        artikel.voegVoorbestellingToe();

        String result = artikel.toString();
        assertThat(result)
                .contains("Voorbestellinggegevens:")
                .contains("Leverdatum:")
                .contains("Minimum aantal:")
                .contains("Aantal voorbestellingen:");
    }

    private ArtikelInVoorbestelling createTestArtikel() {
        return createTestArtikel(LocalDate.now().plusMonths(1));
    }

    private ArtikelInVoorbestelling createTestArtikel(LocalDate leverDatum) {
        return new ArtikelInVoorbestelling(
                "43660", "ESU V60", "ESU", "Test lok", false,
                new BigDecimal("449.99"), new BigDecimal("55.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("697.49"),
                List.of("43660.jpg"), leverDatum, 10
        );
    }
}