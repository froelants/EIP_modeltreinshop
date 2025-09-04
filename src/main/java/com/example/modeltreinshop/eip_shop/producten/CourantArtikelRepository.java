package com.example.modeltreinshop.eip_shop.producten;

import com.example.modeltreinshop.eip_shop.producten.CourantArtikel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourantArtikelRepository extends JpaRepository<CourantArtikel, String> {
    Optional<CourantArtikel> findById(String artikelnummer);
}