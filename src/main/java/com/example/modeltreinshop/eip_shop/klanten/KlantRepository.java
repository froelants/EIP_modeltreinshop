package com.example.modeltreinshop.eip_shop.klanten;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KlantRepository extends JpaRepository<Klant, Long> {
    @Override
    Optional<Klant> findById(Long aLong);
    Optional<Klant> findByEmail(String email);
}