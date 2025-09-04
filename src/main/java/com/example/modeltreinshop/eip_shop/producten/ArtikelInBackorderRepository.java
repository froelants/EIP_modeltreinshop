package com.example.modeltreinshop.eip_shop.producten;

import com.example.modeltreinshop.eip_shop.producten.ArtikelInBackorder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtikelInBackorderRepository extends JpaRepository<ArtikelInBackorder, String> {
    @Override
    Optional<ArtikelInBackorder> findById(String s);
    Optional<ArtikelInBackorder> findByArtikelnummer(String artikelnummer);

}
