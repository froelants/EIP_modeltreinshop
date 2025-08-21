package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ArtikelInVoorbestelling Tests")
class ArtikelInVoorbestellingTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/ArtikelInVoorbestelling_ValidData.csv", numLinesToSkip = 1)
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
            LocalDate besteldatum,
            BigDecimal voorbestellingsPrijs,
            LocalDate voorbestellingTotDatum,
            String leveringsKwartaalStr,
            BigDecimal voorschot,
            boolean zonderLeveringstijd) {

        List<String> afbeeldingenList = List.of(afbeeldingen.split(";"));
        YearMonth leveringsKwartaal = YearMonth.parse(leveringsKwartaalStr.replace("Q", "M3"));

        ArtikelInVoorbestelling artikel = new ArtikelInVoorbestelling(
                artikelnummer, naam, merk, omschrijving, gratisArtikel,
                aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
                afbeeldingenList, besteldatum, voorbestellingsPrijs,
                voorbestellingTotDatum, leveringsKwartaal, voorschot,
                zonderLeveringstijd);

        assertThat(artikel.getArtikelnummer()).isEqualTo(artikelnummer);
        assertThat(artikel.getNaam()).isEqualTo(naam);
        assertThat(artikel.getMerk()).isEqualTo(merk);
        assertThat(artikel.getBesteldatum()).isEqualTo(besteldatum);
        assertThat(artikel.getVoorbestellingsPrijs()).isEqualTo(voorbestellingsPrijs);
        assertThat(artikel.getVoorbestellingTotDatum()).isEqualTo(voorbestellingTotDatum);
        assertThat(artikel.getLeveringsKwartaal()).isEqualTo(zonderLeveringstijd ? null : leveringsKwartaal);
        assertThat(artikel.getVoorschot()).isEqualTo(zonderLeveringstijd ? BigDecimal.ZERO : voorschot);
    }

    @Test
    @DisplayName("Should apply correct pricing logic based on voorbestellingTotDatum")
    void shouldApplyCorrectPricingLogic() {
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now().plusMonths(1),  // besteldatum
                LocalDate.now().plusMonths(2),  // voorbestellingTotDatum
                new BigDecimal("599.99"),      // voorbestellingsPrijs
                false                          // zonderLeveringstijd
                                                           );

        assertThat(artikel.getVerkoopprijs())
                .isEqualTo(new BigDecimal("599.99")); // Should return voorbestellingsPrijs

        // Set voorbestellingTotDatum to past date
        artikel.setVoorbestellingTotDatum(LocalDate.now().minusDays(1));
        assertThat(artikel.getVerkoopprijs())
                .isEqualTo(new BigDecimal("674.99")); // Should return normal verkoopprijs
    }

    @Test
    @DisplayName("Should handle maximum voorbestellingen correctly")
    void shouldHandleMaximumVoorbestellingenCorrectly() {
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now().plusMonths(1),
                LocalDate.now().plusMonths(2),
                new BigDecimal("599.99"),
                false
                                                           );

        artikel.setMaximaalAantalVoorbestellingen(5);
        artikel.setAantalInVoorbestelling(5);

        assertThat(artikel.isVoorbestellingMogelijk()).isFalse();
        assertThat(artikel.getVerkoopprijs())
                .isEqualTo(new BigDecimal("674.99")); // Should return normal verkoopprijs
    }

    @Test
    @DisplayName("Should handle voorschot correctly for articles without leveringstijd")
    void shouldHandleVoorschotForZonderLeveringstijd() {
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now().plusMonths(1),
                LocalDate.now().plusMonths(2),
                new BigDecimal("599.99"),
                true  // zonderLeveringstijd
                                                           );

        assertThat(artikel.getVoorschot()).isEqualTo(BigDecimal.ZERO);
        assertThat(artikel.getLeveringsKwartaal()).isNull();

        // Attempting to set voorschot should still result in 0
        artikel.setVoorschot(new BigDecimal("100.00"));
        assertThat(artikel.getVoorschot()).isEqualTo(BigDecimal.ZERO);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ArtikelInVoorbestelling_InvalidPrices.csv", numLinesToSkip = 1)
    @DisplayName("Should validate voorbestellingsPrijs correctly")
    void shouldValidateVoorbestellingsPrijs(
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
            LocalDate besteldatum,
            BigDecimal voorbestellingsPrijs,
            LocalDate voorbestellingTotDatum,
            String leveringsKwartaalStr,
            BigDecimal voorschot,
            boolean zonderLeveringstijd) {

        List<String> afbeeldingenList = List.of(afbeeldingen.split(";"));
        YearMonth leveringsKwartaal = YearMonth.parse(leveringsKwartaalStr.replace("Q", "M3"));

        assertThatThrownBy(() -> new ArtikelInVoorbestelling(
                artikelnummer, naam, merk, omschrijving, gratisArtikel,
                aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
                afbeeldingenList, besteldatum, voorbestellingsPrijs,
                voorbestellingTotDatum, leveringsKwartaal, voorschot,
                zonderLeveringstijd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Voorbestellingsprijs moet hoger zijn dan minimale verkoopprijs");
    }

    @Test
    @DisplayName("Should validate voorbestellingTotDatum correctly")
    void shouldValidateVoorbestellingTotDatum() {
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                new BigDecimal("599.99"),
                false
                                                           );

        assertThatThrownBy(() -> artikel.setVoorbestellingTotDatum(LocalDate.now().minusDays(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Voorbestellingsdatum moet na besteldatum liggen");
    }

    @Test
    @DisplayName("Should format toString correctly")
    void shouldFormatToStringCorrectly() {
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                new BigDecimal("599.99"),
                false
                                                           );

        String result = artikel.toString();
        assertThat(result)
                .contains("Voorbestelling details:")
                .contains("Besteldatum:")
                .contains("Voorbestellingsprijs:")
                .contains("Voorbestelling mogelijk tot:")
                .contains("Verwachte levering:");
    }

    @Test
    @DisplayName("Should apply normal price after voorbestellingTotDatum")
    void shouldApplyNormalPriceAfterVoorbestellingTotDatum() {
        // Arrange
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now().minusMonths(2),  // besteldatum in past
                LocalDate.now().minusMonths(1),  // voorbestellingTotDatum in past
                new BigDecimal("599.99"),        // voorbestellingsPrijs
                false                            // zonderLeveringstijd
                                                           );

        // Act & Assert
        assertThat(artikel.getVerkoopprijs())
                .as("Should return normal verkoopprijs after voorbestellingTotDatum")
                .isEqualTo(new BigDecimal("674.99"));
    }

    @Test
    @DisplayName("Should validate setMaximaalAantalVoorbestellingen with valid values")
    void shouldValidateSetMaximaalAantalVoorbestellingenWithValidValues() {
        // Arrange
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                new BigDecimal("599.99"),
                false
                                                           );

        // Act & Assert
        // Test setting null (unlimited)
        artikel.setMaximaalAantalVoorbestellingen(null);
        assertThat(artikel.getMaximaalAantalVoorbestellingen()).isNull();

        // Test setting valid positive number
        artikel.setMaximaalAantalVoorbestellingen(10);
        assertThat(artikel.getMaximaalAantalVoorbestellingen()).isEqualTo(10);

        // Test setting equal to current aantal
        artikel.setAantalInVoorbestelling(5);
        artikel.setMaximaalAantalVoorbestellingen(5);
        assertThat(artikel.getMaximaalAantalVoorbestellingen()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should throw exception when setting maximaalAantalVoorbestellingen to zero or negative")
    void shouldThrowExceptionWhenSettingMaximaalAantalVoorbestellingenToZeroOrNegative() {
        // Arrange
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                new BigDecimal("599.99"),
                false
                                                           );

        // Act & Assert
        assertThatThrownBy(() -> artikel.setMaximaalAantalVoorbestellingen(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Maximaal aantal voorbestellingen moet positief zijn");

        assertThatThrownBy(() -> artikel.setMaximaalAantalVoorbestellingen(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Maximaal aantal voorbestellingen moet positief zijn");
    }

    @Test
    @DisplayName("Should throw exception when setting maximaalAantalVoorbestellingen below current aantal")
    void shouldThrowExceptionWhenSettingMaximumBelowCurrentAantal() {
        // Arrange
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                new BigDecimal("599.99"),
                false
                                                           );

        // Set initial state
        artikel.setMaximaalAantalVoorbestellingen(10);
        artikel.setAantalInVoorbestelling(7);

        // Act & Assert
        assertThatThrownBy(() -> artikel.setMaximaalAantalVoorbestellingen(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Maximaal aantal voorbestellingen moet groter zijn dan het huidige aantal in voorbestelling");
    }

    @Test
    @DisplayName("Should allow voorbestelling when under maximum")
    void shouldAllowVoorbestellingWhenUnderMaximum() {
        // Arrange
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                new BigDecimal("599.99"),
                false
                                                           );
        artikel.setMaximaalAantalVoorbestellingen(5);
        artikel.setAantalInVoorbestelling(4);

        // Act & Assert
        assertThat(artikel.isVoorbestellingMogelijk())
                .as("Should allow voorbestelling when under maximum")
                .isTrue();
        assertThat(artikel.getVerkoopprijs())
                .as("Should use voorbestellingsPrijs when under maximum")
                .isEqualTo(new BigDecimal("599.99"));
    }

    @Test
    @DisplayName("Should throw exception when setting aantalInVoorbestelling above maximum")
    void shouldThrowExceptionWhenSettingAboveMaximum() {
        // Arrange
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                new BigDecimal("599.99"),
                false
                                                           );
        artikel.setMaximaalAantalVoorbestellingen(5);
        artikel.setAantalInVoorbestelling(5);

        // Act & Assert
        assertThatThrownBy(() -> artikel.setAantalInVoorbestelling(6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Aantal in voorbestelling kan niet hoger zijn dan het maximum");

        assertThat(artikel.getVerkoopprijs())
                .as("Should use normal verkoopprijs when maximum is reached")
                .isEqualTo(new BigDecimal("674.99"));
    }

    @Test
    @DisplayName("Should handle voorbestellingen with null maximaalAantalVoorbestellingen")
    void shouldHandleNullMaximaalAantalVoorbestellingen() {
        // Arrange
        ArtikelInVoorbestelling artikel = createTestArtikel(
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                new BigDecimal("599.99"),
                false
                                                           );
        artikel.setMaximaalAantalVoorbestellingen(null);
        artikel.setAantalInVoorbestelling(100);  // Any number should be allowed

        // Act & Assert
        assertThat(artikel.isVoorbestellingMogelijk())
                .as("Should allow voorbestelling when maximum is null")
                .isTrue();
        assertThat(artikel.getVerkoopprijs())
                .as("Should use voorbestellingsPrijs when maximum is null")
                .isEqualTo(new BigDecimal("599.99"));
    }

    private ArtikelInVoorbestelling createTestArtikel(
            LocalDate besteldatum,
            LocalDate voorbestellingTotDatum,
            BigDecimal voorbestellingsPrijs,
            boolean zonderLeveringstijd) {
        return new ArtikelInVoorbestelling(
                "73141",
                "BR 01.10 Schnellzug-Dampflok",
                "Roco",
                "DB BR 01.10 mit Ölfeuerung Sound",
                false,
                new BigDecimal("449.99"),
                new BigDecimal("50.00"),
                WinstmargeType.PERCENTAGE,
                new BigDecimal("674.99"),
                List.of("73141-1.jpg", "73141-2.jpg"),
                besteldatum,
                voorbestellingsPrijs,
                voorbestellingTotDatum,
                zonderLeveringstijd ? null : YearMonth.now().plusMonths(3),
                zonderLeveringstijd ? BigDecimal.ZERO : new BigDecimal("100.00"),
                zonderLeveringstijd
        );
    }
}