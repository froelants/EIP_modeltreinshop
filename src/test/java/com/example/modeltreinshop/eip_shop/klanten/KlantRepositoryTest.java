package com.example.modeltreinshop.eip_shop.klanten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Sql("/test-data.sql")
@DisplayName("KlantRepository Integratietests")
class KlantRepositoryTest {

    @Autowired
    private KlantRepository klantRepository;

    @Autowired
    private LandRepository landRepository;

    @Test
    @DisplayName("Moet een Klant opslaan en vinden met gerelateerd Land")
    void shouldSaveAndFindKlant() {
        // Arrange
        Land duitsland = landRepository.findById("DE")
                                       .orElseThrow(() -> new NoSuchElementException("Land met code 'DE' niet gevonden."));
        Adres adres = new Adres("Musterstraße", "5", null, "12345", "Musterstadt", duitsland);
        Klant nieuweKlant = new Klant("Max Mustermann", "max.mustermann@example.de", adres);
        nieuweKlant.setLeveringsadres(adres);

        // Act
        klantRepository.save(nieuweKlant);
        Optional<Klant> gevondenKlant = klantRepository.findById(nieuweKlant.getKlantnummer());

        // Assert
        assertThat(gevondenKlant).isPresent();
        assertThat(gevondenKlant.get().getNaam()).isEqualTo("Max Mustermann");
        assertThat(gevondenKlant.get().getFacturatieadres().getLand().getBtwPercentage().getPercentage()).isEqualTo(new BigDecimal("19.00"));
    }

    @Test
    @DisplayName("Moet alle klanten vinden")
    void shouldFindAllKlanten() {
        // Arrange is gedaan in het @Sql-script
        // Act
        List<Klant> klanten = klantRepository.findAll();
        // Assert
        assertThat(klanten).hasSize(2);
    }
}