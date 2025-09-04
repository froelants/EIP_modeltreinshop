package com.example.modeltreinshop.eip_shop.klanten;

import com.example.modeltreinshop.eip_shop.btw.BtwPercentage;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Klant Business Class Tests")
class KlantTest {

    private Validator validator;
    private Land belgium;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        BtwPercentage btwBelgie = new BtwPercentage(new BigDecimal("21.00"));
        belgium = new Land("BE", "België", btwBelgie);
    }

    @Test
    @DisplayName("Moet een geldige Klant aanmaken")
    void shouldCreateValidKlant() {
        Adres adres = new Adres("Teststraat", "1", null, "1000", "Brussel", belgium);
        Klant klant = new Klant("Jan Jansen", "jan.jansen@example.com", adres);

        assertThat(klant.getNaam()).isEqualTo("Jan Jansen");
        assertThat(klant.getEmail()).isEqualTo("jan.jansen@example.com");
        assertThat(klant.getFacturatieadres()).isEqualTo(adres);
        assertThat(klant.getLeveringsadres()).isNull();
        assertThat(klant.getWinkelmandje()).isNotNull();
    }
}