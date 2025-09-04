package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PrijsComponent Tests")
class PrijsComponentTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @ParameterizedTest
        @CsvFileSource(resources = "/PrijsComponent_ValidConstructorParams.csv", numLinesToSkip = 1)
        @DisplayName("Should create valid PrijsComponent")
        void shouldCreateValidPrijsComponent(
                BigDecimal aankoopprijs,
                BigDecimal minimaleWinstmarge,
                WinstmargeType type,
                BigDecimal expectedMinVerkoopprijs) {

            PrijsComponent component = new PrijsComponent(aankoopprijs, minimaleWinstmarge, type);

            assertThat(component.getAankoopprijs()).isEqualByComparingTo(aankoopprijs);
            assertThat(component.getMinimaleWinstmarge()).isEqualByComparingTo(minimaleWinstmarge);
            assertThat(component.getWinstmargeType()).isEqualTo(type);
            assertThat(component.getMinimaleVerkoopprijs()).isEqualByComparingTo(expectedMinVerkoopprijs);
        }

        @ParameterizedTest
        @MethodSource("invalidConstructorParameters")
        @DisplayName("Should throw exception for invalid constructor parameters")
        void shouldThrowExceptionForInvalidConstructorParameters(
                BigDecimal aankoopprijs,
                BigDecimal minimaleWinstmarge,
                WinstmargeType type,
                String expectedMessage) {

            assertThatThrownBy(() -> new PrijsComponent(aankoopprijs, minimaleWinstmarge, type))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(expectedMessage);
        }
    }

    @Nested
    @DisplayName("Gratis Artikel Tests")
    class GratisArtikelTests {
        @Test
        @DisplayName("Should create gratis artikel")
        void shouldCreateGratisArtikel() {
            PrijsComponent gratisComponent = PrijsComponent.createGratisArtikel();

            assertThat(gratisComponent.isGratisArtikel()).isTrue();
            assertThat(gratisComponent.getAankoopprijs()).isZero();
            assertThat(gratisComponent.getMinimaleWinstmarge()).isZero();
            assertThat(gratisComponent.getVerkoopprijs()).isZero();
            assertThat(gratisComponent.getWinstmargeType()).isEqualTo(WinstmargeType.EURO);
        }
    }

    @Nested
    @DisplayName("Verkoopprijs Tests")
    class VerkoopprijsTests {
        @ParameterizedTest
        @CsvFileSource(resources = "/PrijsComponent_ValidVerkoopprijs.csv", numLinesToSkip = 1)
        @DisplayName("Should set valid verkoopprijs")
        void shouldSetValidVerkoopprijs(
                BigDecimal aankoopprijs,
                BigDecimal minimaleWinstmarge,
                WinstmargeType type,
                BigDecimal verkoopprijs) {

            PrijsComponent component = new PrijsComponent(aankoopprijs, minimaleWinstmarge, type);
            component.setVerkoopprijs(verkoopprijs);

            assertThat(component.getVerkoopprijs()).isEqualByComparingTo(verkoopprijs);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/PrijsComponent_InvalidVerkoopprijs.csv", numLinesToSkip = 1)
        @DisplayName("Should throw exception for invalid verkoopprijs")
        void shouldThrowExceptionForInvalidVerkoopprijs(
                BigDecimal aankoopprijs,
                BigDecimal minimaleWinstmarge,
                WinstmargeType type,
                BigDecimal verkoopprijs,
                String expectedError) {

            PrijsComponent component = new PrijsComponent(aankoopprijs, minimaleWinstmarge, type);

            assertThatThrownBy(() -> component.setVerkoopprijs(verkoopprijs))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(expectedError);
        }
    }

    @Nested
    @DisplayName("Minimale Verkoopprijs Calculation Tests")
    class MinimaleVerkoopprijsTests {
        @ParameterizedTest
        @CsvFileSource(resources = "/PrijsComponent_MinimaleVerkoopprijsEuro.csv", numLinesToSkip = 1)
        @DisplayName("Should calculate minimale verkoopprijs with euro margin")
        void shouldCalculateMinimaleVerkoopprijsWithEuroMargin(
                BigDecimal aankoopprijs,
                BigDecimal minimaleWinstmarge,
                BigDecimal expectedMinVerkoopprijs) {

            PrijsComponent component = new PrijsComponent(aankoopprijs, minimaleWinstmarge, WinstmargeType.EURO);

            assertThat(component.getMinimaleVerkoopprijs())
                    .isEqualByComparingTo(expectedMinVerkoopprijs);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/PrijsComponent_MinimaleVerkoopprijsPercentage.csv", numLinesToSkip = 1)
        @DisplayName("Should calculate minimale verkoopprijs with percentage margin")
        void shouldCalculateMinimaleVerkoopprijsWithPercentageMargin(
                BigDecimal aankoopprijs,
                BigDecimal minimaleWinstmarge,
                BigDecimal expectedMinVerkoopprijs) {

            PrijsComponent component = new PrijsComponent(aankoopprijs, minimaleWinstmarge, WinstmargeType.PERCENTAGE);

            assertThat(component.getMinimaleVerkoopprijs())
                    .isEqualByComparingTo(expectedMinVerkoopprijs);
        }
    }

    private static Stream<Arguments> invalidConstructorParameters() {
        return Stream.of(
                Arguments.of(null, BigDecimal.TEN, WinstmargeType.EURO,
                             "Prijs parameters mogen niet null zijn"),
                Arguments.of(BigDecimal.TEN, null, WinstmargeType.EURO,
                             "Prijs parameters mogen niet null zijn"),
                Arguments.of(BigDecimal.TEN, BigDecimal.TEN, null,
                             "Prijs parameters mogen niet null zijn"),
                Arguments.of(new BigDecimal("-0.01"), BigDecimal.TEN, WinstmargeType.EURO,
                             "Aankoopprijs moet groter zijn dan of gelijk aan 0"),
                Arguments.of(BigDecimal.TEN, new BigDecimal("-0.01"), WinstmargeType.EURO,
                             "Verkoopprijs moet groter zijn dan of gelijk aan 0")
                        );
    }
}