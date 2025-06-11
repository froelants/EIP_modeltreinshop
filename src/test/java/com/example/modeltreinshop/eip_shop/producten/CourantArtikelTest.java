package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CourantArtikel Tests")
class CourantArtikelTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/CourantArtikel_ValidArticles.csv", numLinesToSkip = 1)
    @DisplayName("Should create CourantArtikel with valid data")
    void shouldCreateValidCourantArtikel(
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
            LocalDate eindDatum) {

        List<String> afbeeldingenList = List.of(afbeeldingen.split(";"));

        CourantArtikel artikel = new CourantArtikel(
                artikelnummer, naam, merk, omschrijving, gratisArtikel,
                aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
                afbeeldingenList, eindDatum);

        assertThat(artikel.getArtikelnummer()).isEqualTo(artikelnummer);
        assertThat(artikel.getNaam()).isEqualTo(naam);
        assertThat(artikel.getMerk()).isEqualTo(merk);
        assertThat(artikel.getEindDatum()).isEqualTo(eindDatum);
        assertThat(artikel.isGeldig()).isTrue();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CourantArtikel_InvalidDates.csv", numLinesToSkip = 1)
    @DisplayName("Should throw exception for past end dates")
    void shouldThrowExceptionForPastEndDates(
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
            LocalDate eindDatum) {

        List<String> afbeeldingenList = List.of(afbeeldingen.split(";"));

        assertThatThrownBy(() -> new CourantArtikel(
                artikelnummer, naam, merk, omschrijving, gratisArtikel,
                aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
                afbeeldingenList, eindDatum))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Einddatum moet in de toekomst liggen");
    }

    @Test
    @DisplayName("Should throw exception when end date is null")
    void shouldThrowExceptionWhenEndDateIsNull() {
        assertThatThrownBy(() -> new CourantArtikel(
                "73141", "Roco NS 1200", "Roco", "Test lok", false,
                new BigDecimal("199.99"), new BigDecimal("25.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("249.99"),
                List.of("73141.jpg"), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Einddatum mag niet null zijn");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should throw exception for invalid artikelnummer")
    void shouldThrowExceptionForInvalidArtikelnummer(String invalidArtikelnummer) {
        LocalDate validEndDate = LocalDate.now().plusMonths(1);

        assertThatThrownBy(() -> new CourantArtikel(
                invalidArtikelnummer, "Roco NS 1200", "Roco", "Test lok", false,
                new BigDecimal("199.99"), new BigDecimal("25.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("249.99"),
                List.of("73141.jpg"), validEndDate))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should determine validity based on end date")
    void shouldDetermineValidityBasedOnEndDate() {
        CourantArtikel artikel = new CourantArtikel(
                "73141", "Roco NS 1200", "Roco", "Test lok", false,
                new BigDecimal("199.99"), new BigDecimal("25.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("249.99"),
                List.of("73141.jpg"), LocalDate.now().plusDays(1));

        assertThat(artikel.isGeldig()).isTrue();

        // Wait until just before midnight to test the boundary
        if (LocalDate.now().plusDays(1).equals(artikel.getEindDatum())) {
            assertThat(artikel.isGeldig()).isTrue();
        }
    }

    @Test
    @DisplayName("Should calculate remaining days correctly")
    void shouldCalculateRemainingDaysCorrectly() {
        LocalDate endDate = LocalDate.now().plusDays(30);
        CourantArtikel artikel = new CourantArtikel(
                "73141", "Roco NS 1200", "Roco", "Test lok", false,
                new BigDecimal("199.99"), new BigDecimal("25.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("249.99"),
                List.of("73141.jpg"), endDate);

        long expectedDays = LocalDate.now().until(endDate).getDays();
        assertThat(artikel.getResterendeDagen()).isEqualTo(expectedDays);
    }
}