package com.example.modeltreinshop.eip_shop.producten.entities;

import com.jouwbedrijf.artikelbeheer.domain.CourantArtikel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CourantArtikelRepository extends JpaRepository<CourantArtikel, Long> {
    Optional<CourantArtikel> findByArtikelInVoorraad_ArtikelId(Long artikelId);
}