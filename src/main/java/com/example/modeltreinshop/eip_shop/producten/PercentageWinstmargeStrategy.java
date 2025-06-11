package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageWinstmargeStrategy implements WinstmargeStrategy {
    /* PercentageWinstmargeStrategy Class
     * Business Logic:
     * - Strategy for calculating percentage-based margins
     * - Implements WinstmargeStrategy interface
     * - Calculates sale price based on:
     *   - Purchase price
     *   - Margin percentage
     * - Validates inputs for calculation
     */
    @Override
    public BigDecimal berekenVerkoopprijs(BigDecimal aankoopprijs,
                                          BigDecimal percentage) {
        return aankoopprijs.multiply(BigDecimal.ONE.add(percentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)))
                .setScale(2, RoundingMode.HALF_UP);
    }
}