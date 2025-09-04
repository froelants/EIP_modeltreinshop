package com.example.modeltreinshop.eip_shop.producten;

import com.example.modeltreinshop.eip_shop.producten.Artikel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtikelRepository extends JpaRepository<Artikel, String> {
    Optional<Artikel> findById(String artikelnummer);

}