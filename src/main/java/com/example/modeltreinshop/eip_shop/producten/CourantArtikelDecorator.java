package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CourantArtikelDecoratorDecorator extends ArtikelInVoorraadDecorator {
    private boolean inKorting;
    private LocalDate kortingTotDatum;
    private int kortingTotRest;
    private BigDecimal prijsInKorting;
    private BigDecimal maximaleKorting;
    private int minimaleVoorraad;
    private int normaleVoorraad;
    private int minimaleBestelhoeveelheid;
    private PrijsComponent prijsComponent;

    public CourantArtikelDecoratorDecorator(Artikel artikel, LocalDate laatsteAankoopdatum,
                                            BigDecimal aankoopprijs, BigDecimal minimaleWinstmarge,
                                            WinstmargeType winstmargeType, int minimaleVoorraad,
                                            int normaleVoorraad, int bestelpunt) {
        super(artikel, laatsteAankoopdatum, aankoopprijs, minimaleWinstmarge, winstmargeType);
        this.prijsComponent = new PrijsComponent(aankoopprijs, minimaleWinstmarge, winstmargeType);
        if (minimaleVoorraad <= 0) {
            throw new IllegalArgumentException("Minimale voorraad moet groter zijn dan 0");
        }
        if (normaleVoorraad <= minimaleVoorraad) {
            throw new IllegalArgumentException("Normale voorraad moet groter zijn dan minimale voorraad");
        }
        if (bestelpunt < 0) {
            throw new IllegalArgumentException("Minimale bestelhoeveelheid mag niet negatief zijn");
        }

        this.minimaleVoorraad = minimaleVoorraad;
        this.normaleVoorraad = normaleVoorraad;
        this.minimaleBestelhoeveelheid = bestelpunt;
        this.inKorting = false;
    }


    @Override
    public BigDecimal getVerkoopprijs() {
        return isInKorting() ? prijsInKorting : super.getVerkoopprijs();
    }

    protected void prijsGewijzigd(BigDecimal nieuweAankoopprijs) {
        if (isInKorting()) {
            BigDecimal minimaleVerkoopprijs = nieuweAankoopprijs.add(getMinimaleWinstmarge());
            if (prijsInKorting.compareTo(minimaleVerkoopprijs) < 0) {
                stopKorting();
            }
        }
    }

    public void startKorting(LocalDate kortingTotDatum, int kortingTotRest,
                             BigDecimal prijsInKorting) {
        if (kortingTotDatum == null || kortingTotDatum.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Kortingsdatum moet in de toekomst liggen");
        }
        if (kortingTotRest <= 0) {
            throw new IllegalArgumentException("Aantal in korting moet positief zijn");
        }
        if (prijsInKorting == null) {
            throw new IllegalArgumentException("Prijs in korting mag niet null zijn");
        }

        // Calculate price bounds
        BigDecimal minimaleKortingsprijs = getLaatsteAankoopprijs().add(getMinimaleWinstmarge());
        // Validate korting price is between aankoopprijs and verkoopprijs
        if (prijsInKorting.compareTo(getLaatsteAankoopprijs()) <= 0) {
            throw new IllegalArgumentException("Prijs in korting moet hoger zijn dan aankoopprijs");
        }
        if (prijsInKorting.compareTo(getVerkoopprijs()) >= 0) {
            throw new IllegalArgumentException("Prijs in korting moet lager zijn dan verkoopprijs");
        }

        this.kortingTotDatum = kortingTotDatum;
        this.kortingTotRest = kortingTotRest;
        this.prijsInKorting = prijsInKorting;
        this.inKorting = true;
    }


    public void stopKorting() {
        this.inKorting = false;
        this.kortingTotDatum = null;
        this.kortingTotRest = 0;
        this.prijsInKorting = null;
        this.maximaleKorting = null;
    }

    public boolean isInKorting() {
        // Check of korting verlopen is
        if (inKorting && (LocalDate.now().isAfter(kortingTotDatum) || kortingTotRest == 0)) {
            stopKorting();
        }
        return inKorting;
    }

    public void verminderKortingRest(int aantal) {
        if (!isInKorting()) {
            throw new IllegalStateException("Artikel is niet in korting");
        }
        if (aantal < 0) {
            throw new IllegalArgumentException("Aantal mag niet negatief zijn");
        }
        if (aantal > kortingTotRest) {
            throw new IllegalArgumentException("Aantal is groter dan beschikbare kortingrest");
        }
        kortingTotRest -= aantal;
        if (kortingTotRest == 0) {
            stopKorting();
        }
    }

    // Getters voor korting-gerelateerde velden
    public LocalDate getKortingTotDatum() {
        return isInKorting() ? kortingTotDatum : null;
    }

    public int getKortingTotRest() {
        return isInKorting() ? kortingTotRest : 0;
    }

    public BigDecimal getPrijsInKorting() {
        return isInKorting() ? prijsInKorting : null;
    }

    public BigDecimal getMaximaleKorting() {
        return isInKorting() ? maximaleKorting : null;
    }

    // Voorraad-gerelateerde getters en setters
    public int getMinimaleVoorraad() {
        return minimaleVoorraad;
    }

    public void setMinimaleVoorraad(int minimaleVoorraad) {
        if (minimaleVoorraad <= 0) {
            throw new IllegalArgumentException("Minimale voorraad moet groter zijn dan 0");
        }
        if (minimaleVoorraad >= normaleVoorraad) {
            throw new IllegalArgumentException("Minimale voorraad moet kleiner zijn dan normale voorraad");
        }
        this.minimaleVoorraad = minimaleVoorraad;
    }

    public int getNormaleVoorraad() {
        return normaleVoorraad;
    }

    public void setNormaleVoorraad(int normaleVoorraad) {
        if (normaleVoorraad <= minimaleVoorraad) {
            throw new IllegalArgumentException("Normale voorraad moet groter zijn dan minimale voorraad");
        }
        this.normaleVoorraad = normaleVoorraad;
    }

    public int getMinimaleBestelhoeveelheid() {
        return minimaleBestelhoeveelheid;
    }

    public void setMinimaleBestelhoeveelheid(int minimaleBestelhoeveelheid) {
        if (minimaleBestelhoeveelheid < 0) {
            throw new IllegalArgumentException("Minimale bestelhoeveelheid mag niet negatief zijn");
        }
        this.minimaleBestelhoeveelheid = minimaleBestelhoeveelheid;
    }
    public void setAankoopprijs(BigDecimal nieuweAankoopprijs) {
        // Update PrijsComponent
        prijsComponent.setAankoopprijs(nieuweAankoopprijs);
        // Check if korting is still valid after price change
        if (isInKorting()) {
            BigDecimal minimaleVerkoopprijs = nieuweAankoopprijs.add(getMinimaleWinstmarge());
            if (prijsInKorting.compareTo(minimaleVerkoopprijs) < 0) {
                stopKorting();
            }
        }
        // Update base price
        super.setAankoopprijs(nieuweAankoopprijs);
    }
}