package com.example.modeltreinshop.eip_shop.producten.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PercentageWinstmargeStrategyTest {
    private final PercentageWinstmargeStrategy strategy = new PercentageWinstmargeStrategy();

    @Test
    void berekenVerkoopprijs_Met100Procent() {
        assertEquals(
                new BigDecimal("200.00"),
                strategy.berekenVerkoopprijs(
                        new BigDecimal("100.00"),
                        new BigDecimal("100.00")
                )
        );
    }

    @Test
    void berekenVerkoopprijs_Met0Procent() {
        assertEquals(
                new BigDecimal("100.00"),
                strategy.berekenVerkoopprijs(
                        new BigDecimal("100.00"),
                        new BigDecimal("0.00")
                )
        );
    }

    @Test
    void berekenVerkoopprijs_MetOneven_RondtCorrectAf() {
        assertEquals(
                new BigDecimal("125.41"),
                strategy.berekenVerkoopprijs(
                        new BigDecimal("100.33"),
                        new BigDecimal("25.00")
                )
        );
    }
}
