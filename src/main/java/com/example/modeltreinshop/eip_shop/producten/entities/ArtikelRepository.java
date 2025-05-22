package com.example.modeltreinshop.eip_shop.producten.entities;

import com.jouwbedrijf.artikelbeheer.domain.Artikel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArtikelRepository extends JpaRepository<Artikel, Long> {
    Optional<Artikel> findByNummer(String nummer);
}
