package com.example.modeltreinshop.eip_shop.btw;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class BtwPercentage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long btwId;

    @Column(precision = 5, scale = 2)
    @NotNull(message = "BTW percentage mag niet null zijn")
    @PositiveOrZero(message = "BTW percentage moet groter dan of gelijk aan 0 zijn")
    private BigDecimal percentage;

    public BtwPercentage() {}

    public BtwPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    // Getters en setters
    public Long getBtwId() {
        return btwId;
    }

    public void setBtwId(Long btwId) {
        this.btwId = btwId;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BtwPercentage that = (BtwPercentage) o;
        return Objects.equals(btwId, that.btwId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(btwId);
    }
}