package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/artikel/artikel_in_backorder_repository_test.sql")
@DisplayName("ArtikelInBackorderRepository Integratietests")
class ArtikelInBackorderRepositoryTest {

    @Autowired
    private ArtikelInBackorderRepository artikelInBackorderRepository;

    @Test
    @DisplayName("Moet een ArtikelInBackorder opslaan en vinden")
    void shouldSaveAndFindArtikelInBackorder() {
        // Arrange is gedaan in het SQL-script
        String artikelnummer = "58469";

        // Act
        Optional<ArtikelInBackorder> artikelOptional = artikelInBackorderRepository.findById(artikelnummer);

        // Assert
        assertThat(artikelOptional).isPresent();
        ArtikelInBackorder gevondenArtikel = artikelOptional.get();
        assertThat(gevondenArtikel.getArtikelnummer()).isEqualTo(artikelnummer);
        assertThat(gevondenArtikel.getAantalInBackorder()).isEqualTo(8);
    }
}