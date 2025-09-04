package com.example.modeltreinshop.eip_shop.klanten;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LandRepository extends JpaRepository<Land, String> {
    Optional<Land> findByNaam(String naam);
}