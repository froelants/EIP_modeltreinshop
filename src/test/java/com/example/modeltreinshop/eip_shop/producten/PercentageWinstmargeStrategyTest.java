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

@DisplayName("PercentageWinstmargeStrategy Tests")
class PercentageWinstmargeStrategyTest {

    private final PercentageWinstmargeStrategy strategy = new PercentageWinstmargeStrategy();

    @Nested
    @DisplayName("Sales Price Calculation Tests")
    class SalesPriceCalculationTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/percentage_strategy_calculations.csv", numLinesToSkip = 1)
        @DisplayName("Should calculate correct sales price")
        void shouldCalculateCorrectSalesPrice(
                BigDecimal purchasePrice,
                BigDecimal marginPercentage,
                BigDecimal expectedSalesPrice) {

            BigDecimal actualSalesPrice = strategy.berekenVerkoopprijs(purchasePrice, marginPercentage)
                                                  .setScale(2, RoundingMode.HALF_UP);

            assertThat(actualSalesPrice).isEqualByComparingTo(expectedSalesPrice);
        }

        @Test
        @DisplayName("Should calculate sales price with zero percentage")
        void shouldCalculateSalesPriceWithZeroPercentage() {
            BigDecimal purchasePrice = new BigDecimal("199.99");
            BigDecimal percentage = BigDecimal.ZERO;

            BigDecimal salesPrice = strategy.berekenVerkoopprijs(purchasePrice, percentage);

            assertThat(salesPrice).isEqualByComparingTo(purchasePrice);
        }

        @Test
        @DisplayName("Should calculate sales price with 100% margin")
        void shouldCalculateSalesPriceWith100PercentMargin() {
            BigDecimal purchasePrice = new BigDecimal("199.99");
            BigDecimal percentage = new BigDecimal("100");

            BigDecimal salesPrice = strategy.berekenVerkoopprijs(purchasePrice, percentage);

            assertThat(salesPrice).isEqualByComparingTo(purchasePrice.multiply(new BigDecimal("2")));
        }

        @Test
        @DisplayName("Should handle maximum values")
        void shouldHandleMaximumValues() {
            BigDecimal maxPurchasePrice = new BigDecimal("999999.99");
            BigDecimal maxPercentage = new BigDecimal("999.99");

            BigDecimal salesPrice = strategy.berekenVerkoopprijs(maxPurchasePrice, maxPercentage)
                                            .setScale(2, RoundingMode.HALF_UP);

            assertThat(salesPrice).isGreaterThan(maxPurchasePrice);
        }
    }

    @Nested
    @DisplayName("Parameter Validation Tests")
    class ParameterValidationTests {

        @ParameterizedTest
        @MethodSource("com.example.modeltreinshop.eip_shop.producten.PercentageWinstmargeStrategyTest#invalidParameters")
        @DisplayName("Should throw exception for invalid parameters")
        void shouldThrowExceptionForInvalidParameters(
                BigDecimal purchasePrice,
                BigDecimal percentage,
                String expectedMessage) {

            assertThatThrownBy(() -> strategy.berekenVerkoopprijs(purchasePrice, percentage))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(expectedMessage);
        }

        @Test
        @DisplayName("Should handle minimum valid values")
        void shouldHandleMinimumValidValues() {
            BigDecimal minPurchasePrice = BigDecimal.ZERO;
            BigDecimal minPercentage = BigDecimal.ZERO;

            BigDecimal salesPrice = strategy.berekenVerkoopprijs(minPurchasePrice, minPercentage);

            assertThat(salesPrice).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    @DisplayName("Real-world Product Tests")
    class RealWorldProductTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/real_products_percentage.csv", numLinesToSkip = 1)
        @DisplayName("Should calculate correct prices for real products")
        void shouldCalculateCorrectPricesForRealProducts(
                String productName,
                BigDecimal purchasePrice,
                BigDecimal marginPercentage,
                BigDecimal expectedSalesPrice) {

            BigDecimal actualSalesPrice = strategy.berekenVerkoopprijs(purchasePrice, marginPercentage)
                                                  .setScale(2, RoundingMode.HALF_UP);

            assertThat(actualSalesPrice)
                    .as("Sales price calculation for %s", productName)
                    .isEqualByComparingTo(expectedSalesPrice);
        }

        @Test
        @DisplayName("Should handle fractional percentages")
        void shouldHandleFractionalPercentages() {
            BigDecimal purchasePrice = new BigDecimal("199.99");
            BigDecimal percentage = new BigDecimal("12.5");

            BigDecimal salesPrice = strategy.berekenVerkoopprijs(purchasePrice, percentage)
                                            .setScale(2, RoundingMode.HALF_UP);

            BigDecimal expected = purchasePrice.multiply(BigDecimal.ONE.add(percentage.divide(new BigDecimal("100"))))
                                               .setScale(2, RoundingMode.HALF_UP);
            assertThat(salesPrice).isEqualByComparingTo(expected);
        }
    }


    private static Stream<Arguments> invalidParameters() {
        return Stream.of(
                Arguments.of(null, BigDecimal.TEN, "Aankoopprijs mag niet null zijn"),
                Arguments.of(BigDecimal.TEN, null, "Winstmarge percentage mag niet null zijn"),
                Arguments.of(new BigDecimal("-0.01"), BigDecimal.TEN, "Aankoopprijs mag niet negatief zijn"),
                Arguments.of(BigDecimal.TEN, new BigDecimal("-0.01"), "Winstmarge percentage mag niet negatief zijn"),
                Arguments.of(BigDecimal.TEN, new BigDecimal("1000"), "Winstmarge percentage mag niet groter zijn dan 999.99")
                        );
    }
}