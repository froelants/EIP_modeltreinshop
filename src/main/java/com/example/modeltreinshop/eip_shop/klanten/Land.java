package com.example.modeltreinshop.eip_shop.klanten;

import com.example.modeltreinshop.eip_shop.btw.BtwPercentage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Land {

    @Id
    @Column(length = 2)
    @NotBlank(message = "Landcode mag niet leeg zijn")
    private String landcode;

    @Column
    @NotBlank(message = "Landnaam mag niet leeg zijn")
    private String landnaam;

    @ManyToOne
    @NotNull(message = "BTW percentage mag niet null zijn")
    @JoinColumn(name = "btw_id")
    private BtwPercentage btwPercentage;

    public Land() {}

    public Land(String landcode, String landnaam, BtwPercentage btwPercentage) {
        this.landcode = landcode;
        this.landnaam = landnaam;
        this.btwPercentage = btwPercentage;
    }

    // Getters en setters
    public String getLandcode() {
        return landcode;
    }

    public void setLandcode(String landcode) {
        this.landcode = landcode;
    }

    public String getLandnaam() {
        return landnaam;
    }

    public void setLandnaam(String landnaam) {
        this.landnaam = landnaam;
    }

    public BtwPercentage getBtwPercentage() {
        return btwPercentage;
    }

    public void setBtwPercentage(BtwPercentage btwPercentage) {
        this.btwPercentage = btwPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Land land = (Land) o;
        return Objects.equals(landcode, land.landcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(landcode);
    }
}