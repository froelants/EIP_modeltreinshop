package com.example.modeltreinshop.eip_shop.winkelen;

import com.example.modeltreinshop.eip_shop.winkelen.Winkelmandje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WinkelmandjeRepository extends JpaRepository<Winkelmandje, Long> {
}