package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/artikel/artikel_repository_test.sql")
@DisplayName("ArtikelRepository Tests")
class ArtikelRepositoryTest {

    @Autowired
    private ArtikelRepository artikelRepository;

    // Een concrete test-implementatie van de abstracte klasse Artikel
    private static class TestArtikel extends Artikel {
        public TestArtikel(String artikelnummer,
                           String naam,
                           String merk,
                           String omschrijving,
                           boolean gratisArtikel,
                           BigDecimal aankoopprijs,
                           BigDecimal winstmarge,
                           WinstmargeType type,
                           BigDecimal verkoopprijs,
                           List<String> afbeeldingen) {
            super(artikelnummer,
                  naam,
                  merk,
                  omschrijving,
                  gratisArtikel,
                  aankoopprijs,
                  winstmarge,
                  type,
                  verkoopprijs,
                  afbeeldingen);
        }
    }

    @Test
    @DisplayName("Should find an Artikel by artikelnummer")
    void shouldFindArtikelByArtikelnummer() {
        // Arrange is done by the @Sql annotation
        String artikelnummer = "73141";

        // Act
        Optional<Artikel> artikelOptional = artikelRepository.findById(artikelnummer);

        // Assert
        assertThat(artikelOptional).isPresent();
        assertThat(artikelOptional.get().getArtikelnummer()).isEqualTo(artikelnummer);
    }

    @Test
    @DisplayName("Should save a new Artikel")
    void shouldSaveNewArtikel() {
        // Arrange
        Artikel newArtikel = new TestArtikel(
                "NEW123",
                "Test Artikel",
                "Test Merk",
                "Een nieuw test artikel.",
                false,
                new BigDecimal("100.00"),
                new BigDecimal("20.00"),
                WinstmargeType.PERCENTAGE,
                new BigDecimal("120.00"),
                List.of("test_image.jpg"));

        // Act
        Artikel savedArtikel = artikelRepository.save(newArtikel);

        // Assert
        assertThat(savedArtikel).isNotNull();
        assertThat(savedArtikel.getArtikelnummer()).isEqualTo("NEW123");
    }

    @Test
    @DisplayName("Should delete an Artikel")
    void shouldDeleteArtikel() {
        // Arrange
        String artikelnummer = "73141";

        // Act
        artikelRepository.deleteById(artikelnummer);

        // Assert
        Optional<Artikel> artikelOptional = artikelRepository.findById(artikelnummer);
        assertThat(artikelOptional).isNotPresent();
    }
}