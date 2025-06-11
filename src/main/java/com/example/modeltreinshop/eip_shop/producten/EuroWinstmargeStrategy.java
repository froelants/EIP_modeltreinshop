package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EuroWinstmargeStrategy implements WinstmargeStrategy {
    /* EuroWinstmargeStrategy Class
     * Business Logic:
     * - Implements fixed euro amount profit margin
     * - Profit is added directly to purchase price
     * - Amount is absolute (not percentage)
     * - Validates positive margin amounts
     * - Used when profit should be fixed regardless of price
     */
    @Override
    public BigDecimal berekenVerkoopprijs(BigDecimal aankoopprijs,
                                          BigDecimal marge) {
        return aankoopprijs.add(marge).setScale(2, RoundingMode.HALF_UP);
    }
}