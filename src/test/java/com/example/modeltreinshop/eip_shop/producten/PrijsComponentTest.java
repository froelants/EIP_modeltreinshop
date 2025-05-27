package be.vdab.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PrijsComponentTest {

    @ParameterizedTest
    @CsvSource({
            "100.00, 20.00, EURO, false",       // Geldige waarden met EURO
            "100.00, 20.00, PERCENTAGE, false", // Geldige waarden met PERCENTAGE
            "0.00, 20.00, EURO, true",         // Ongeldige aankoopprijs (0)
            "-1.00, 20.00, EURO, true",        // Negatieve aankoopprijs
            "100.00, -1.00, EURO, true",       // Negatieve winstmarge
            "null, 20.00, EURO, true",         // Null aankoopprijs
            "100.00, null, EURO, true",        // Null winstmarge
            "100.00, 20.00, null, true"        // Null winstmargetype
    })
    void constructor_ValidatieWaarden(String aankoopprijs, String winstmarge,
                                      String type, boolean verwachtException) {
        BigDecimal testAankoopprijs = "null".equals(aankoopprijs) ?
                null : new BigDecimal(aankoopprijs);
        BigDecimal testWinstmarge = "null".equals(winstmarge) ?
                null : new BigDecimal(winstmarge);
        WinstmargeType testType = "null".equals(type) ?
                null : WinstmargeType.valueOf(type);

        if (verwachtException) {
            assertThrows(IllegalArgumentException.class, () ->
                    new PrijsComponent(testAankoopprijs, testWinstmarge, testType));
        } else {
            PrijsComponent component = new PrijsComponent(
                    testAankoopprijs, testWinstmarge, testType);
            assertEquals(testAankoopprijs.setScale(2), component.getAankoopprijs());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "100.00, 20.00, EURO, 120.00",
            "100.00, 20.00, PERCENTAGE, 120.00",
            "100.00, 0.00, EURO, 100.00",
            "99.99, 10.00, EURO, 109.99"
    })
    void berekenVerkoopprijs_CorrecteBerekeningPerType(String aankoopprijs,
                                                       String winstmarge, String type, String verwachteVerkoopprijs) {
        PrijsComponent component = new PrijsComponent(
                new BigDecimal(aankoopprijs),
                new BigDecimal(winstmarge),
                WinstmargeType.valueOf(type)
        );

        assertEquals(new BigDecimal(verwachteVerkoopprijs),
                     component.berekenVerkoopprijs());
    }

    @Test
    void getters_RetournerenCorrecteWaarden() {
        PrijsComponent component = new PrijsComponent(
                new BigDecimal("100.00"),
                new BigDecimal("20.00"),
                WinstmargeType.EURO
        );

        assertEquals(new BigDecimal("100.00"), component.getAankoopprijs());
        assertEquals(new BigDecimal("20.00"), component.getMinimaleWinstmarge());
        assertEquals(WinstmargeType.EURO, component.getWinstmargeType());
    }
}
