package com.example.modeltreinshop.eip_shop.producten;

import com.example.modeltreinshop.eip_shop.producten.ArtikelInVoorraad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtikelInVoorraadRepository extends JpaRepository<ArtikelInVoorraad, String> {
    Optional<ArtikelInVoorraad> findById(String artikelnummer);
}