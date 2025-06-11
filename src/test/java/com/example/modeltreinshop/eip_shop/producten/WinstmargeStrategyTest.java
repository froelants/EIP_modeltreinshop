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

@DisplayName("WinstmargeStrategy Tests")
class WinstmargeStrategyTest {

    private final WinstmargeStrategy euroStrategy = new EuroWinstmargeStrategy();
    private final WinstmargeStrategy percentageStrategy = new PercentageWinstmargeStrategy();

    @Nested
    @DisplayName("Strategy Type Tests")
    class StrategyTypeTests {

        @Nested
        @DisplayName("Sales Price Calculation Tests")
        class SalesPriceCalculationTests {

            @ParameterizedTest
            @CsvFileSource(resources = "/strategy_calculations.csv", numLinesToSkip = 1)
            @DisplayName("Should calculate correct sales prices using both strategies")
            void shouldCalculateCorrectSalesPricesUsingBothStrategies(
                    BigDecimal purchasePrice,
                    BigDecimal euroMargin,
                    BigDecimal percentageMargin,
                    BigDecimal expectedEuroPrice,
                    BigDecimal expectedPercentagePrice) {

                BigDecimal actualEuroPrice = euroStrategy
                        .berekenVerkoopprijs(purchasePrice,
                                             euroMargin)
                        .setScale(2,
                                  RoundingMode.HALF_UP);
                BigDecimal actualPercentagePrice = percentageStrategy
                        .berekenVerkoopprijs(purchasePrice,
                                             percentageMargin)
                        .setScale(2,
                                  RoundingMode.HALF_UP);

                assertThat(actualEuroPrice).isEqualByComparingTo(expectedEuroPrice);
                assertThat(actualPercentagePrice).isEqualByComparingTo(expectedPercentagePrice);
            }
        }

        @Nested
        @DisplayName("Real-world Product Tests")
        class RealWorldProductTests {

            @ParameterizedTest
            @CsvFileSource(resources = "real_products_strategy.csv", numLinesToSkip = 1)
            @DisplayName("Should calculate correct prices for real products using both strategies")
            void shouldCalculateCorrectPricesForRealProducts(
                    String productName,
                    BigDecimal purchasePrice,
                    BigDecimal euroMargin,
                    BigDecimal percentageMargin,
                    BigDecimal expectedEuroPrice,
                    BigDecimal expectedPercentagePrice) {

                BigDecimal actualEuroPrice = euroStrategy
                        .berekenVerkoopprijs(purchasePrice,
                                             euroMargin)
                        .setScale(2,
                                  RoundingMode.HALF_UP);
                BigDecimal actualPercentagePrice = percentageStrategy
                        .berekenVerkoopprijs(purchasePrice,
                                             percentageMargin)
                        .setScale(2,
                                  RoundingMode.HALF_UP);

                assertThat(actualEuroPrice)
                        .as("Euro strategy price for %s",
                            productName)
                        .isEqualByComparingTo(expectedEuroPrice);
                assertThat(actualPercentagePrice)
                        .as("Percentage strategy price for %s",
                            productName)
                        .isEqualByComparingTo(expectedPercentagePrice);
            }
        }

        @Nested
        @DisplayName("Edge Cases Tests")
        class EdgeCasesTests {

            @ParameterizedTest
            @MethodSource("com.example.modeltreinshop.eip_shop.producten.WinstmargeStrategyTest#invalidParameters")
            @DisplayName("Should throw exception for invalid parameters in both strategies")
            void shouldThrowExceptionForInvalidParameters(
                    BigDecimal purchasePrice,
                    BigDecimal margin,
                    String expectedMessage) {

                assertThatThrownBy(() -> euroStrategy.berekenVerkoopprijs(purchasePrice,
                                                                          margin))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(expectedMessage);

                assertThatThrownBy(() -> percentageStrategy.berekenVerkoopprijs(purchasePrice,
                                                                                margin))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(expectedMessage);
            }

            @Test
            @DisplayName("Should handle zero values in both strategies")
            void shouldHandleZeroValues() {
                BigDecimal purchasePrice = BigDecimal.ZERO;
                BigDecimal margin = BigDecimal.ZERO;

                assertThat(euroStrategy.berekenVerkoopprijs(purchasePrice,
                                                            margin))
                        .isEqualByComparingTo(BigDecimal.ZERO);
                assertThat(percentageStrategy.berekenVerkoopprijs(purchasePrice,
                                                                  margin))
                        .isEqualByComparingTo(BigDecimal.ZERO);
            }

            @Test
            @DisplayName("Should handle maximum values in both strategies")
            void shouldHandleMaximumValues() {
                BigDecimal maxPrice = new BigDecimal("999999.99");
                BigDecimal euroMargin = new BigDecimal("999999.99");
                BigDecimal percentageMargin = new BigDecimal("999.99");

                assertThat(euroStrategy.berekenVerkoopprijs(maxPrice,
                                                            euroMargin))
                        .isGreaterThan(maxPrice);
                assertThat(percentageStrategy.berekenVerkoopprijs(maxPrice,
                                                                  percentageMargin))
                        .isGreaterThan(maxPrice);
            }
        }

        private static Stream<Arguments> invalidParameters() {
            return Stream.of(
                    Arguments.of(null,
                                 BigDecimal.TEN,
                                 "mag niet null zijn"),
                    Arguments.of(BigDecimal.TEN,
                                 null,
                                 "mag niet null zijn"),
                    Arguments.of(new BigDecimal("-0.01"),
                                 BigDecimal.TEN,
                                 "mag niet negatief zijn"),
                    Arguments.of(BigDecimal.TEN,
                                 new BigDecimal("-0.01"),
                                 "mag niet negatief zijn")
                            );
        }
    }
}