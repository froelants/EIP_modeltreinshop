package com.example.modeltreinshop.eip_shop.producten.model;

import java.math.BigDecimal;

public interface WinstmargeStrategy {
    BigDecimal berekenVerkoopprijs(BigDecimal aankoopprijs, BigDecimal marge);
}
