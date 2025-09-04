package com.example.modeltreinshop.eip_shop.producten;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/artikel/artikel_in_voorbestelling_repository_test.sql")
@DisplayName("ArtikelInVoorbestellingRepository Integratietests")
class ArtikelInVoorbestellingRepositoryTest {

    @Autowired
    private ArtikelInVoorbestellingRepository artikelInVoorbestellingRepository;

    @Test
    @DisplayName("Moet een ArtikelInVoorbestelling opslaan en vinden")
    void shouldSaveAndFindArtikelInVoorbestelling() {
        // Arrange is gedaan in het SQL-script
        String artikelnummer = "43279-2";

        // Act
        Optional<ArtikelInVoorbestelling> artikelOptional = artikelInVoorbestellingRepository.findById(artikelnummer);

        // Assert
        assertThat(artikelOptional).isPresent();
        ArtikelInVoorbestelling gevondenArtikel = artikelOptional.get();
        assertThat(gevondenArtikel.getArtikelnummer()).isEqualTo(artikelnummer);
        assertThat(gevondenArtikel.getVoorbestellingsPrijs()).isEqualTo(new BigDecimal("799.99"));
    }
}