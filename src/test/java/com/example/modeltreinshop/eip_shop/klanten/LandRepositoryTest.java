package com.example.modeltreinshop.eip_shop.klanten;

import com.example.modeltreinshop.eip_shop.btw.BtwPercentage;
import com.example.modeltreinshop.eip_shop.btw.repository.BtwPercentageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/test-data.sql")
@DisplayName("LandRepository Integratietests")
class LandRepositoryTest {

    @Autowired
    private LandRepository landRepository;
    @Autowired
    private BtwPercentageRepository btwPercentageRepository;

    @Test
    @DisplayName("Moet een Land vinden op landcode en gerelateerd BTW percentage")
    void shouldFindLandByCode() {
        // Arrange is done by the @Sql annotation
        String landcode = "BE";

        // Act
        Optional<Land> land = landRepository.findById(landcode);

        // Assert
        assertThat(land).isPresent();
        assertThat(land.get().getLandnaam()).isEqualTo("België");
        assertThat(land.get().getBtwPercentage().getPercentage()).isEqualTo(new BigDecimal("21.00"));
    }

    @Test
    @DisplayName("Moet een nieuw Land opslaan met een nieuw BTW percentage")
    void shouldSaveNewLandWithNewBtwPercentage() {
        // Arrange
        BtwPercentage btwNL = new BtwPercentage(new BigDecimal("21.00"));
        btwPercentageRepository.save(btwNL);
        Land nieuwLand = new Land("NL", "Nederland", btwNL);

        // Act
        Land opgeslagenLand = landRepository.save(nieuwLand);

        // Assert
        assertThat(opgeslagenLand.getLandcode()).isEqualTo("NL");
        assertThat(landRepository.findAll()).hasSize(3);
    }

    @Test
    @DisplayName("Moet een land vinden op naam")
    void shouldFindLandByNaam() {
        // Arrange
        String landnaam = "België";

        // Act
        Optional<Land> landOptional = landRepository.findByNaam(landnaam);

        // Assert
        assertThat(landOptional).isPresent();
        assertThat(landOptional.get().getLandcode()).isEqualTo("BE");
    }
}