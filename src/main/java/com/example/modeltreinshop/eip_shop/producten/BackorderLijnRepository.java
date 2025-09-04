package com.example.modeltreinshop.eip_shop.producten;

import com.example.modeltreinshop.eip_shop.producten.BackorderLijn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackorderLijnRepository extends JpaRepository<BackorderLijn, Long> {
    @Override
    Optional<BackorderLijn> findById(Long aLong);
}