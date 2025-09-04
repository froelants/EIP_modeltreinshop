package com.example.modeltreinshop.eip_shop.producten;

public enum WinstmargeType {
    /* Winstmarge Class
     * Business Logic:
     * - Encapsulates profit margin logic
     * - Holds margin amount and type
     * - Types:
     *   - EURO: fixed amount
     *   - PERCENTAGE: relative to price
     * - Validates margin values
     * - Used in price calculations
     */
    EURO(new EuroWinstmargeStrategy()),
    PERCENTAGE(new PercentageWinstmargeStrategy());

    private final WinstmargeStrategy strategy;

    WinstmargeType(WinstmargeStrategy strategy) {
        this.strategy = strategy;
    }

    public WinstmargeStrategy getStrategy() {
        return strategy;
    }

}