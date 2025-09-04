package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/artikel/artikel_in_voorraad_repository_test.sql")
@DisplayName("ArtikelInVoorraadRepository Integratietests")
class ArtikelInVoorraadRepositoryTest {

    @Autowired
    private ArtikelInVoorraadRepository artikelInVoorraadRepository;

    @Test
    @DisplayName("Moet een ArtikelInVoorraad opslaan en vinden")
    void shouldSaveAndFindArtikelInVoorraad() {
        // Arrange is gedaan in het SQL-script
        String artikelnummer = "73916";

        // Act
        Optional<ArtikelInVoorraad> artikelOptional = artikelInVoorraadRepository.findById(artikelnummer);

        // Assert
        assertThat(artikelOptional).isPresent();
        ArtikelInVoorraad gevondenArtikel = artikelOptional.get();
        assertThat(gevondenArtikel.getArtikelnummer()).isEqualTo(artikelnummer);
        assertThat(gevondenArtikel.getVoorraad()).isEqualTo(10);
    }
}