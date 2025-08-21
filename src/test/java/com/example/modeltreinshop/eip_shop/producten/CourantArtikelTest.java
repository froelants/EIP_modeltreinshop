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
            int voorraad,
            int minimaleVoorraad,
            int normaleVoorraad,
            int minimaleBestelhoeveelheid) {

        List<String> afbeeldingenList = List.of(afbeeldingen.split(";"));

        CourantArtikel artikel = new CourantArtikel(
                artikelnummer,
                naam,
                merk,
                omschrijving,
                gratisArtikel,
                aankoopprijs,
                winstmarge,
                winstmargeType,
                verkoopprijs,
                afbeeldingenList,
                voorraad,
                minimaleVoorraad,
                normaleVoorraad,
                minimaleBestelhoeveelheid);

        assertThat(artikel.getArtikelnummer()).isEqualTo(artikelnummer);
        assertThat(artikel.getMinimaleVoorraad()).isEqualTo(minimaleVoorraad);
        assertThat(artikel.getNormaleVoorraad()).isEqualTo(normaleVoorraad);
        assertThat(artikel.getMinimaleBestelhoeveelheid()).isEqualTo(minimaleBestelhoeveelheid);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CourantArtikel_InvalidStockParams.csv", numLinesToSkip = 1)
    @DisplayName("Should throw exception for invalid stock parameters")
    void shouldThrowExceptionForInvalidStockParams(
            int minimaleVoorraad,
            int normaleVoorraad,
            int minimaleBestelhoeveelheid,
            String expectedError) {

        assertThatThrownBy(() -> new CourantArtikel(
                "73141",
                "BR 01.10 Dampflok",
                "Roco",
                "DB BR 01.10 Sound",
                false,
                new BigDecimal("449.99"),
                new BigDecimal("50.00"),
                WinstmargeType.PERCENTAGE,
                new BigDecimal("674.99"),
                List.of("73141.jpg"),
                10, // voorraad
                minimaleVoorraad,
                normaleVoorraad,
                minimaleBestelhoeveelheid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedError);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CourantArtikel_InvalidStockParams.csv", numLinesToSkip = 1)
    @DisplayName("Should throw exception for invalid stock parameters")
    void shouldThrowExceptionForInvalidArtikelnummer(
            int voorraad,
            int minimaleVoorraad,
            int normaleVoorraad,
            int minimaleBestelhoeveelheid,
            String expectedError) {

        assertThatThrownBy(() -> new CourantArtikel(
                "73141",
                "BR 01.10 Dampflok",
                "Roco",
                "DB BR 01.10 Sound",
                false,
                new BigDecimal("449.99"),
                new BigDecimal("50.00"),
                WinstmargeType.PERCENTAGE,
                new BigDecimal("674.99"),
                List.of("73141.jpg"),
                voorraad,
                minimaleVoorraad,
                normaleVoorraad,
                minimaleBestelhoeveelheid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedError);
    }
}