package com.example.modeltreinshop.eip_shop.producten;

import com.example.modeltreinshop.eip_shop.producten.ArtikelInVoorbestelling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtikelInVoorbestellingRepository extends JpaRepository<ArtikelInVoorbestelling, String> {
    Optional<ArtikelInVoorbestelling> findById(String artikelnummer);
    Optional<ArtikelInVoorbestelling> findByArtikelnummer(String artikelnummer);
}