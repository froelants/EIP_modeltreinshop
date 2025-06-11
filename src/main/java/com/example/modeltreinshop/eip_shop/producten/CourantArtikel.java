package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/*
 2025/05/27 11:15
 */
public class CourantArtikel
        extends ArtikelInVoorraad {
    /* CourantArtikel Class
     * Business Logic:
     * - Represents regularly stocked articles
     * - Extends Artikel with standard pricing
     * - Uses normal profit margin calculation
     * - Regular stock management applies
     * - Standard pricing rules apply
     */
    private int minimaleVoorraad;
    private int normaleVoorraad;
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
        super(artikelnummer,
              naam,
              merk,
              omschrijving,
              gratisArtikel,
              aankoopprijs,
              winstmarge,
              winstmargeType,
              verkoopprijs,
              afbeeldingen,
              voorraad);
        if (minimaleVoorraad <= 0 || normaleVoorraad <= minimaleVoorraad || minimaleBestelhoeveelheid <= 0) {
            throw new IllegalArgumentException("Ongeldige voorraadparameters");
        }
        this.minimaleVoorraad = minimaleVoorraad;
        this.normaleVoorraad = normaleVoorraad;
        this.minimaleBestelhoeveelheid = minimaleBestelhoeveelheid;
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

    private void valideerVoorraadParameters(int minimaleVoorraad,
                                            int normaleVoorraad,
                                            int minimaleBestelhoeveelheid) {
        List<String> errors = new ArrayList<>();

        if (minimaleVoorraad <= 0) {
            errors.add("Minimale voorraad moet positief zijn");
        }
        if (normaleVoorraad <= minimaleVoorraad) {
            errors.add("Normale voorraad moet groter zijn dan minimale voorraad");
        }
        if (minimaleBestelhoeveelheid <= 0) {
            errors.add("Minimale bestelhoeveelheid moet positief zijn");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(" en ",
                                                           errors));
        }
    }

    public void setMinimaleVoorraad(int minimaleVoorraad) {
        valideerVoorraadParameters(minimaleVoorraad,
                                   this.normaleVoorraad,
                                   this.minimaleBestelhoeveelheid);
        this.minimaleVoorraad = minimaleVoorraad;
    }

    public void setNormaleVoorraad(int normaleVoorraad) {
        valideerVoorraadParameters(this.minimaleVoorraad,
                                   normaleVoorraad,
                                   this.minimaleBestelhoeveelheid);
        this.normaleVoorraad = normaleVoorraad;
    }

    public void setMinimaleBestelhoeveelheid(int minimaleBestelhoeveelheid) {
        valideerVoorraadParameters(this.minimaleVoorraad,
                                   this.normaleVoorraad,
                                   minimaleBestelhoeveelheid);
        this.minimaleBestelhoeveelheid = minimaleBestelhoeveelheid;
    }
}