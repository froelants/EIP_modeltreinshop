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

@DisplayName("EuroWinstmargeStrategy Tests")
class EuroWinstmargeStrategyTest {

    private final EuroWinstmargeStrategy strategy = new EuroWinstmargeStrategy();

    @Nested
    @DisplayName("Sales Price Calculation Tests")
    class SalesPriceCalculationTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/winstmarge/euro_strategy_calculations.csv", numLinesToSkip = 1)
        @DisplayName("Should calculate correct sales price")
        void shouldCalculateCorrectSalesPrice(
                BigDecimal purchasePrice,
                BigDecimal marginAmount,
                BigDecimal expectedSalesPrice) {

            BigDecimal actualSalesPrice = strategy.berekenVerkoopprijs(purchasePrice, marginAmount)
                                                  .setScale(2, RoundingMode.HALF_UP);

            assertThat(actualSalesPrice).isEqualByComparingTo(expectedSalesPrice);
        }

        @Test
        @DisplayName("Should calculate sales price with zero margin")
        void shouldCalculateSalesPriceWithZeroMargin() {
            BigDecimal purchasePrice = new BigDecimal("199.99");
            BigDecimal margin = BigDecimal.ZERO;

            BigDecimal salesPrice = strategy.berekenVerkoopprijs(purchasePrice, margin);

            assertThat(salesPrice).isEqualByComparingTo(purchasePrice);
        }

        @Test
        @DisplayName("Should handle maximum values")
        void shouldHandleMaximumValues() {
            BigDecimal maxPurchasePrice = new BigDecimal("999999.99");
            BigDecimal maxMargin = new BigDecimal("999999.99");

            BigDecimal salesPrice = strategy.berekenVerkoopprijs(maxPurchasePrice, maxMargin);

            assertThat(salesPrice)
                    .isGreaterThan(maxPurchasePrice)
                    .isEqualByComparingTo(maxPurchasePrice.add(maxMargin));
        }
    }

    @Nested
    @DisplayName("Parameter Validation Tests")
    class ParameterValidationTests {

        @ParameterizedTest
        @MethodSource("com.example.modeltreinshop.eip_shop.producten.EuroWinstmargeStrategyTest#invalidParameters")
        @DisplayName("Should throw exception for invalid parameters")
        void shouldThrowExceptionForInvalidParameters(
                BigDecimal purchasePrice,
                BigDecimal margin,
                String expectedMessage) {

            assertThatThrownBy(() -> strategy.berekenVerkoopprijs(purchasePrice, margin))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(expectedMessage);
        }

        @Test
        @DisplayName("Should handle minimum valid values")
        void shouldHandleMinimumValidValues() {
            BigDecimal minPurchasePrice = BigDecimal.ZERO;
            BigDecimal minMargin = BigDecimal.ZERO;

            BigDecimal salesPrice = strategy.berekenVerkoopprijs(minPurchasePrice, minMargin);

            assertThat(salesPrice).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    @DisplayName("Real-world Product Tests")
    class RealWorldProductTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/winstmarge/real_products_euro_strategy.csv", numLinesToSkip = 1)
        @DisplayName("Should calculate correct prices for real products")
        void shouldCalculateCorrectPricesForRealProducts(
                String productName,
                BigDecimal purchasePrice,
                BigDecimal marginAmount,
                BigDecimal expectedSalesPrice) {

            BigDecimal actualSalesPrice = strategy.berekenVerkoopprijs(purchasePrice, marginAmount)
                                                  .setScale(2, RoundingMode.HALF_UP);

            assertThat(actualSalesPrice)
                    .as("Sales price calculation for %s", productName)
                    .isEqualByComparingTo(expectedSalesPrice);
        }
    }

    @Test
    @DisplayName("Should return EURO as type")
    void shouldReturnEuroAsType() {
        assertThat(strategy.getType()).isEqualTo(WinstmargeType.EURO);
    }

    private static Stream<Arguments> invalidParameters() {
        return Stream.of(
                Arguments.of(null, BigDecimal.TEN, "Aankoopprijs mag niet null zijn"),
                Arguments.of(BigDecimal.TEN, null, "Winstmarge bedrag mag niet null zijn"),
                Arguments.of(new BigDecimal("-0.01"), BigDecimal.TEN, "Aankoopprijs mag niet negatief zijn"),
                Arguments.of(BigDecimal.TEN, new BigDecimal("-0.01"), "Winstmarge bedrag mag niet negatief zijn")
                        );
    }
}