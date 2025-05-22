package com.example.modeltreinshop.eip_shop.producten.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WinstmargeStrategyTest {
    private final WinstmargeStrategy euroStrategy = new EuroWinstmargeStrategy();
    private final WinstmargeStrategy percentageStrategy = new PercentageWinstmargeStrategy();

    @ParameterizedTest
    @CsvSource({
            "100.00, 20.00, 120.00",    // Standaard berekening
            "100.00, 0.00, 100.00",     // Geen winstmarge
            "100.00, 50.00, 150.00",    // Hogere winstmarge
            "99.99, 10.00, 109.99"      // Afrondingstest
    })
    void euroStrategy_BerekenVerkoopprijs(String aankoopprijs,
                                          String winstmarge, String verwachteVerkoopprijs) {
        assertEquals(
                new BigDecimal(verwachteVerkoopprijs),
                euroStrategy.berekenVerkoopprijs(
                        new BigDecimal(aankoopprijs),
                        new BigDecimal(winstmarge)
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "100.00, 20.00, 120.00",    // 20% winstmarge
            "100.00, 0.00, 100.00",     // 0% winstmarge
            "100.00, 50.00, 150.00",    // 50% winstmarge
            "99.99, 10.00, 109.99"      // Afrondingstest
    })
    void percentageStrategy_BerekenVerkoopprijs(String aankoopprijs,
                                                String percentage, String verwachteVerkoopprijs) {
        assertEquals(
                new BigDecimal(verwachteVerkoopprijs),
                percentageStrategy.berekenVerkoopprijs(
                        new BigDecimal(aankoopprijs),
                        new BigDecimal(percentage)
                )
        );
    }
}
