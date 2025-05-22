package com.example.modeltreinshop.eip_shop.producten.entities;

import com.jouwbedrijf.artikelbeheer.domain.WinstmargeType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WinstmargeTypeRepository extends JpaRepository<WinstmargeType, Integer> {
    Optional<WinstmargeType> findByTypeNaam(String typeNaam);
}
