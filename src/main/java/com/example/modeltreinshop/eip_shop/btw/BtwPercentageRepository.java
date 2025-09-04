package com.example.modeltreinshop.eip_shop.btw.repository;

import com.example.modeltreinshop.eip_shop.btw.BtwPercentage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BtwPercentageRepository extends JpaRepository<BtwPercentage, Long> {
}