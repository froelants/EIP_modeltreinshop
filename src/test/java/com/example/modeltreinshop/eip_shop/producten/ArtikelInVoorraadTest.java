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

@DisplayName("ArtikelInVoorraad Tests")
class ArtikelInVoorraadTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/ArtikelInVoorraad_ValidStock.csv", numLinesToSkip = 1)
    @DisplayName("Should create ArtikelInVoorraad with valid data")
    void shouldCreateValidArtikelInVoorraad(
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
            int voorraad) {

        List<String> afbeeldingenList = List.of(afbeeldingen.split(";"));

        ArtikelInVoorraad artikel = new ArtikelInVoorraad(
                artikelnummer, naam, merk, omschrijving, gratisArtikel,
                aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
                afbeeldingenList, voorraad);

        assertThat(artikel.getArtikelnummer()).isEqualTo(artikelnummer);
        assertThat(artikel.getNaam()).isEqualTo(naam);
        assertThat(artikel.getMerk()).isEqualTo(merk);
        assertThat(artikel.getVoorraad()).isEqualTo(voorraad);
        assertThat(artikel.getLaatsteAankoopdatum()).isEqualTo(LocalDate.now());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ArtikelInVoorraad_InvalidStock.csv", numLinesToSkip = 1)
    @DisplayName("Should throw exception for invalid voorraad")
    void shouldThrowExceptionForInvalidVoorraad(
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
            int voorraad) {

        List<String> afbeeldingenList = List.of(afbeeldingen.split(";"));

        assertThatThrownBy(() -> new ArtikelInVoorraad(
                artikelnummer, naam, merk, omschrijving, gratisArtikel,
                aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
                afbeeldingenList, voorraad))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Voorraad mag niet negatief zijn");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ArtikelInVoorraad_StockModifications.csv", numLinesToSkip = 1)
    @DisplayName("Should correctly modify voorraad")
    void shouldCorrectlyModifyVoorraad(
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
            int initialVoorraad,
            int wijziging,
            int expectedVoorraad) {

        List<String> afbeeldingenList = List.of(afbeeldingen.split(";"));
        ArtikelInVoorraad artikel = new ArtikelInVoorraad(
                artikelnummer, naam, merk, omschrijving, gratisArtikel,
                aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
                afbeeldingenList, initialVoorraad);

        artikel.wijzigVoorraad(wijziging);
        assertThat(artikel.getVoorraad()).isEqualTo(expectedVoorraad);
    }

    @Test
    @DisplayName("Should set voorraad when current is zero")
    void shouldSetVoorraadWhenCurrentIsZero() {
        ArtikelInVoorraad artikel = new ArtikelInVoorraad(
                "73916", "BR 193", "Roco", "Test lok", false,
                new BigDecimal("259.99"), new BigDecimal("40.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("363.99"),
                List.of("73916-1.jpg"), 0);

        artikel.setVoorraad(5);
        assertThat(artikel.getVoorraad()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should throw exception when setting voorraad with non-zero current")
    void shouldThrowExceptionWhenSettingVoorraadWithNonZeroCurrent() {
        ArtikelInVoorraad artikel = new ArtikelInVoorraad(
                "73916", "BR 193", "Roco", "Test lok", false,
                new BigDecimal("259.99"), new BigDecimal("40.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("363.99"),
                List.of("73916-1.jpg"), 10);

        assertThatThrownBy(() -> artikel.setVoorraad(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("De huidige voorraad moet 0 zijn");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    @DisplayName("Should throw exception when setting negative voorraad")
    void shouldThrowExceptionWhenSettingNegativeVoorraad(int invalidVoorraad) {
        ArtikelInVoorraad artikel = new ArtikelInVoorraad(
                "73916", "BR 193", "Roco", "Test lok", false,
                new BigDecimal("259.99"), new BigDecimal("40.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("363.99"),
                List.of("73916-1.jpg"), 0);

        assertThatThrownBy(() -> artikel.setVoorraad(invalidVoorraad))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("De nieuwe voorraad mag niet negatief zijn");
    }

    @Test
    @DisplayName("Should throw exception when modification results in negative stock")
    void shouldThrowExceptionWhenModificationResultsInNegativeStock() {
        ArtikelInVoorraad artikel = new ArtikelInVoorraad(
                "73916", "BR 193", "Roco", "Test lok", false,
                new BigDecimal("259.99"), new BigDecimal("40.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("363.99"),
                List.of("73916-1.jpg"), 5);

        assertThatThrownBy(() -> artikel.wijzigVoorraad(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Resulterende voorraad mag niet negatief zijn");
    }
}