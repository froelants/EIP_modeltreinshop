package com.example.modeltreinshop.eip_shop.winkelen;

import com.example.modeltreinshop.eip_shop.klanten.Klant;
import com.example.modeltreinshop.eip_shop.producten.Artikel;
import com.example.modeltreinshop.eip_shop.producten.ArtikelRepository;
import com.example.modeltreinshop.eip_shop.klanten.KlantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/test-data.sql")
@DisplayName("WinkelmandjeRepository Integratietests")
class WinkelmandjeRepositoryTest {

    @Autowired
    private WinkelmandjeRepository winkelmandjeRepository;

    @Autowired
    private KlantRepository klantRepository;

    @Autowired
    private ArtikelRepository artikelRepository;

    @Test
    @DisplayName("Moet een Winkelmandje opslaan en koppelen aan een Klant")
    void shouldSaveAndLinkWinkelmandjeToKlant() {
        // Arrange
        Klant klant = klantRepository.findById(1L).get();
        Winkelmandje winkelmandje = new Winkelmandje();
        klant.setWinkelmandje(winkelmandje);

        // Act
        klantRepository.save(klant);
        Optional<Winkelmandje> gevondenWinkelmandje = winkelmandjeRepository.findById(winkelmandje.getId());

        // Assert
        assertThat(gevondenWinkelmandje).isPresent();
        assertThat(gevondenWinkelmandje.get().getKlant().getKlantnummer()).isEqualTo(klant.getKlantnummer());
    }

    @Test
    @DisplayName("Moet artikelen toevoegen aan het Winkelmandje")
    void shouldAddArtikelenToWinkelmandje() {
        // Arrange
        Klant klant = klantRepository.findById(1L).get();
        Winkelmandje winkelmandje = new Winkelmandje();
        klant.setWinkelmandje(winkelmandje);
        klantRepository.save(klant);

        Artikel artikel1 = artikelRepository.findById("73141").get();
        Artikel artikel2 = artikelRepository.findById("43279").get();

        winkelmandje.getArtikelen().add(artikel1);
        winkelmandje.getArtikelen().add(artikel2);

        // Act
        winkelmandjeRepository.save(winkelmandje);

        // Assert
        Optional<Winkelmandje> gevondenWinkelmandje = winkelmandjeRepository.findById(winkelmandje.getId());
        assertThat(gevondenWinkelmandje).isPresent();
        assertThat(gevondenWinkelmandje.get().getArtikelen()).hasSize(2);
    }
}