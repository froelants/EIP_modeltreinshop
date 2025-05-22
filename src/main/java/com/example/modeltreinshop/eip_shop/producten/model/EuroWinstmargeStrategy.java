package com.example.modeltreinshop.eip_shop.producten.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EuroWinstmargeStrategy implements WinstmargeStrategy {
    @Override
    public BigDecimal berekenVerkoopprijs(BigDecimal aankoopprijs, BigDecimal marge) {
        return aankoopprijs.add(marge).setScale(2, RoundingMode.HALF_UP);
    }
}
