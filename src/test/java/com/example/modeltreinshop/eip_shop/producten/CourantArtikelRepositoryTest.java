package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/artikel/courant_artikel_repository_test.sql")
@DisplayName("CourantArtikelRepository Integratietests")
class CourantArtikelRepositoryTest {

    @Autowired
    private CourantArtikelRepository courantArtikelRepository;

    @Test
    @DisplayName("Moet een CourantArtikel opslaan en vinden")
    void shouldSaveAndFindCourantArtikel() {
        // Arrange is gedaan in het SQL-script
        String artikelnummer = "73141";

        // Act
        Optional<CourantArtikel> artikelOptional = courantArtikelRepository.findById(artikelnummer);

        // Assert
        assertThat(artikelOptional).isPresent();
        CourantArtikel gevondenArtikel = artikelOptional.get();
        assertThat(gevondenArtikel.getArtikelnummer()).isEqualTo(artikelnummer);
        assertThat(gevondenArtikel.getVoorraad()).isEqualTo(8);
        assertThat(gevondenArtikel.moetBijbesteld()).isTrue();
    }
}