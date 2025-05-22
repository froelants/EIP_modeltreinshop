package com.example.modeltreinshop.eip_shop.producten.entities;

import com.jouwbedrijf.artikelbeheer.domain.ArtikelInVoorbestelling;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArtikelInVoorbestellingRepository extends JpaRepository<ArtikelInVoorbestelling, Long> {
    Optional<ArtikelInVoorbestelling> findByArtikelId(Long artikelId);
}
