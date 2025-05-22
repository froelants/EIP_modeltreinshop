package com.example.modeltreinshop.eip_shop.producten.entities;

import com.jouwbedrijf.artikelbeheer.domain.ArtikelInBackorder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ArtikelInBackorderRepository extends JpaRepository<ArtikelInBackorder, Long> {
    Optional<ArtikelInBackorder> findByArtikelId(Long artikelId);
    List<ArtikelInBackorder> findByArtikelInVoorraad_ArtikelId(Long artikelInVoorraadId);
}
