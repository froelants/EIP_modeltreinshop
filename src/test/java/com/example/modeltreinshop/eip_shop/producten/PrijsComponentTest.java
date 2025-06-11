package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PrijsComponent Tests")
class PrijsComponentTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/prijscomponent/valid_prices.csv", numLinesToSkip = 1)
        @DisplayName("Should create PrijsComponent with valid parameters")
        void shouldCreatePrijsComponentWithValidParameters(
                BigDecimal aankoopprijs,
                BigDecimal winstmarge,
                WinstmargeType type) {

            PrijsComponent prijsComponent = new PrijsComponent(aankoopprijs, winstmarge, type);

            assertThat(prijsComponent.getAankoopprijs()).isEqualByComparingTo(aankoopprijs);
            assertThat(prijsComponent.getWinstmarge()).isEqualByComparingTo(winstmarge);
            assertThat(prijsComponent.getWinstmargeType()).isEqualTo(type);
        }

        @ParameterizedTest
        @MethodSource("com.example.modeltreinshop.eip_shop.producten.PrijsComponentTest#invalidConstructorParameters")
        @DisplayName("Should throw exception for invalid constructor parameters")
        void shouldThrowExceptionForInvalidConstructorParameters(
                BigDecimal aankoopprijs,
                BigDecimal winstmarge,
                WinstmargeType type,
                String expectedMessage) {

            assertThatThrownBy(() -> new PrijsComponent(aankoopprijs, winstmarge, type))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(expectedMessage);
        }
    }

    @Nested
    @DisplayName("Sales Price Calculation Tests")
    class SalesPriceCalculationTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/prijscomponent/euro_margin_calculations.csv", numLinesToSkip = 1)
        @DisplayName("Should calculate correct sales price with euro margin")
        void shouldCalculateCorrectSalesPriceWithEuroMargin(
                BigDecimal aankoopprijs,
                BigDecimal winstmarge,
                BigDecimal expectedVerkoopprijs) {

            PrijsComponent prijsComponent = new PrijsComponent(aankoopprijs, winstmarge, WinstmargeType.EURO);

            assertThat(prijsComponent.getVerkoopprijs())
                    .isEqualByComparingTo(expectedVerkoopprijs);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/prijscomponent/percentage_margin_calculations.csv", numLinesToSkip = 1)
        @DisplayName("Should calculate correct sales price with percentage margin")
        void shouldCalculateCorrectSalesPriceWithPercentageMargin(
                BigDecimal aankoopprijs,
                BigDecimal winstmarge,
                BigDecimal expectedVerkoopprijs) {

            PrijsComponent prijsComponent = new PrijsComponent(aankoopprijs, winstmarge, WinstmargeType.PERCENTAGE);

            assertThat(prijsComponent.getVerkoopprijs())
                    .isEqualByComparingTo(expectedVerkoopprijs);
        }
    }

    @Nested
    @DisplayName("Real-world Product Tests")
    class RealWorldProductTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/prijscomponent/real_products.csv", numLinesToSkip = 1)
        @DisplayName("Should calculate correct prices for real products")
        void shouldCalculateCorrectPricesForRealProducts(
                String productName,
                BigDecimal aankoopprijs,
                BigDecimal winstmarge,
                WinstmargeType type,
                BigDecimal expectedVerkoopprijs) {

            PrijsComponent prijsComponent = new PrijsComponent(aankoopprijs, winstmarge, type);

            assertThat(prijsComponent.getVerkoopprijs())
                    .as("Sales price calculation for %s", productName)
                    .isEqualByComparingTo(expectedVerkoopprijs);
        }

        @Test
        @DisplayName("Should handle high-end product prices")
        void shouldHandleHighEndProductPrices() {
            BigDecimal aankoopprijs = new BigDecimal("2499.99");
            BigDecimal winstmarge = new BigDecimal("500.00");

            PrijsComponent prijsComponent = new PrijsComponent(aankoopprijs, winstmarge, WinstmargeType.EURO);

            assertThat(prijsComponent.getVerkoopprijs())
                    .isEqualByComparingTo(new BigDecimal("2999.99"));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle zero purchase price")
        void shouldHandleZeroPurchasePrice() {
            PrijsComponent prijsComponent = new PrijsComponent(
                    BigDecimal.ZERO,
                    new BigDecimal("10.00"),
                    WinstmargeType.EURO);

            assertThat(prijsComponent.getVerkoopprijs())
                    .isEqualByComparingTo(new BigDecimal("10.00"));
        }

        @Test
        @DisplayName("Should handle zero margin")
        void shouldHandleZeroMargin() {
            PrijsComponent prijsComponent = new PrijsComponent(
                    new BigDecimal("199.99"),
                    BigDecimal.ZERO,
                    WinstmargeType.PERCENTAGE);

            assertThat(prijsComponent.getVerkoopprijs())
                    .isEqualByComparingTo(new BigDecimal("199.99"));
        }

        @Test
        @DisplayName("Should handle maximum values")
        void shouldHandleMaximumValues() {
            BigDecimal maxPrice = new BigDecimal("999999.99");
            BigDecimal maxMargin = new BigDecimal("999.99");

            PrijsComponent prijsComponent = new PrijsComponent(maxPrice, maxMargin, WinstmargeType.PERCENTAGE);

            assertThat(prijsComponent.getVerkoopprijs()).isGreaterThan(maxPrice);
        }
    }

    private static Stream<Arguments> invalidConstructorParameters() {
        return Stream.of(
                Arguments.of(null, BigDecimal.TEN, WinstmargeType.EURO,
                             "Aankoopprijs mag niet null zijn"),
                Arguments.of(BigDecimal.TEN, null, WinstmargeType.EURO,
                             "Winstmarge mag niet null zijn"),
                Arguments.of(BigDecimal.TEN, BigDecimal.TEN, null,
                             "WinstmargeType mag niet null zijn"),
                Arguments.of(new BigDecimal("-0.01"), BigDecimal.TEN, WinstmargeType.EURO,
                             "Aankoopprijs mag niet negatief zijn"),
                Arguments.of(BigDecimal.TEN, new BigDecimal("-0.01"), WinstmargeType.EURO,
                             "Winstmarge mag niet negatief zijn")
                        );
    }
}