package com.example.modeltreinshop.eip_shop.producten.entities;

import com.jouwbedrijf.artikelbeheer.domain.ArtikelInVoorraad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArtikelInVoorraadRepository extends JpaRepository<ArtikelInVoorraad, Long> {
    // Je kunt hier ook findByArtikel_Id(Long artikelId) of findByArtikel_Nummer(String nummer) gebruiken
    // als je direct via het gekoppelde artikel wilt zoeken.
    Optional<ArtikelInVoorraad> findByArtikelId(Long artikelId);
}