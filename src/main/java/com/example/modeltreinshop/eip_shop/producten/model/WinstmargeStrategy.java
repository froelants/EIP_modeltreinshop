package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;

public interface WinstmargeStrategy {
    BigDecimal berekenVerkoopprijs(BigDecimal aankoopprijs, BigDecimal marge);
}
