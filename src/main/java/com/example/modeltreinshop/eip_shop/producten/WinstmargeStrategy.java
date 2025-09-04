package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
/* WinstmargeStrategy Interface
 * Business Logic:
 * - Strategy pattern for profit margin calculation
 * - Defines contract for margin calculations
 * - Allows different margin calculation methods:
 *   - Fixed euro amount
 *   - Percentage based
 * - Used by Artikel class for price calculations
 */
public interface WinstmargeStrategy {
    BigDecimal berekenVerkoopprijs(BigDecimal aankoopprijs,
                                   BigDecimal marge);

    WinstmargeType getType();
}