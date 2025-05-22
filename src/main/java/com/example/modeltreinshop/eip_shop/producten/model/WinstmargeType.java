package com.example.modeltreinshop.eip_shop.producten;

public enum WinstmargeType {
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