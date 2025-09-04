package com.example.modeltreinshop.eip_shop.producten;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "courant_artikel")
@PrimaryKeyJoinColumn(name = "artikelnummer")
public class CourantArtikel extends ArtikelInVoorraad {

    @Column
    @Positive(message = "Minimale voorraad moet positief zijn")
    private int minimaleVoorraad;

    @Column
    @Positive(message = "Normale voorraad moet groter zijn dan minimale voorraad")
    private int normaleVoorraad;

    @Column
    @Positive(message = "Minimale bestelhoeveelheid moet positief zijn")
    private int minimaleBestelhoeveelheid;

    public CourantArtikel(String artikelnummer,
                          String naam,
                          String merk,
                          String omschrijving,
                          boolean gratisArtikel,
                          BigDecimal aankoopprijs,
                          BigDecimal winstmarge,
                          WinstmargeType winstmargeType,
                          BigDecimal verkoopprijs,
                          List<String> afbeeldingen,
                          int voorraad,
                          int minimaleVoorraad,
                          int normaleVoorraad,
                          int minimaleBestelhoeveelheid) {
        super(artikelnummer, naam, merk, omschrijving, gratisArtikel,
              aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
              afbeeldingen, voorraad);

        this.minimaleVoorraad = minimaleVoorraad;
        this.normaleVoorraad = normaleVoorraad;
        this.minimaleBestelhoeveelheid = minimaleBestelhoeveelheid;
    }

    // No-arg constructor for JPA
    public CourantArtikel() {
        super();
    }

    public boolean moetBijbesteld() {
        return getVoorraad() <= minimaleVoorraad;
    }

    public int getBijbestelAantal() {
        return moetBijbesteld() ? normaleVoorraad - getVoorraad() : 0;
    }

    public int getMinimaleVoorraad() {
        return minimaleVoorraad;
    }

    public int getNormaleVoorraad() {
        return normaleVoorraad;
    }

    public int getMinimaleBestelhoeveelheid() {
        return minimaleBestelhoeveelheid;
    }
}