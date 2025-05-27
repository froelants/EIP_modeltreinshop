package com.example.modeltreinshop.eip_shop.producten.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class EuroWinstmargeStrategyTest {
    private final EuroWinstmargeStrategy strategy = new EuroWinstmargeStrategy();

    @Test
    void berekenVerkoopprijs_MetPositieveWaarden() {
        assertEquals(
                new BigDecimal("120.00"),
                strategy.berekenVerkoopprijs(
                        new BigDecimal("100.00"),
                        new BigDecimal("20.00")
                )
        );
    }

    @Test
    void berekenVerkoopprijs_MetNul() {
        assertEquals(
                new BigDecimal("0.00"),
                strategy.berekenVerkoopprijs(
                        new BigDecimal("0.00"),
                        new BigDecimal("0.00")
                )
        );
    }

    @Test
    void berekenVerkoopprijs_MetOneven_RondtCorrectAf() {
        assertEquals(
                new BigDecimal("120.33"),
                strategy.berekenVerkoopprijs(
                        new BigDecimal("100.33"),
                        new BigDecimal("20.00")
                )
        );
    }
}
