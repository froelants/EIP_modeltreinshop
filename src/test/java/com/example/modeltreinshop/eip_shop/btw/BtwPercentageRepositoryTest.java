package com.example.modeltreinshop.eip_shop.btw;

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
@DisplayName("BtwPercentageRepository Integratietests")
class BtwPercentageRepositoryTest {

    @Autowired
    private com.example.modeltreinshop.eip_shop.btw.repository.BtwPercentageRepository btwPercentageRepository;

    @Test
    @DisplayName("Moet een BTW percentage opslaan en vinden")
    void shouldSaveAndFindBtwPercentage() {
        // Arrange
        BtwPercentage nieuwBtw = new BtwPercentage(new BigDecimal("21.00"));

        // Act
        BtwPercentage opgeslagenBtw = btwPercentageRepository.save(nieuwBtw);
        Optional<BtwPercentage> gevondenBtw = btwPercentageRepository.findById(opgeslagenBtw.getBtwId());

        // Assert
        assertThat(gevondenBtw).isPresent();
        assertThat(gevondenBtw.get().getPercentage()).isEqualByComparingTo(new BigDecimal("21.00"));
    }
}
