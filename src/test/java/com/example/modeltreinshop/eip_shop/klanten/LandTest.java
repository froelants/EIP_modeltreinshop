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

@DisplayName("Land Business Class Tests")
class LandTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Moet een geldig Land aanmaken")
    void shouldCreateValidLand() {
        BtwPercentage btwBelgie = new BtwPercentage(new BigDecimal("21.00"));
        Land land = new Land("BE", "België", btwBelgie);
        assertThat(land.getLandcode()).isEqualTo("BE");
        assertThat(land.getLandnaam()).isEqualTo("België");
        assertThat(land.getBtwPercentage().getPercentage()).isEqualTo(new BigDecimal("21.00"));
    }
}