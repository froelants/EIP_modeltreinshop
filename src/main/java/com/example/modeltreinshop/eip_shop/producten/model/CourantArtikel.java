package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CourantArtikel extends ArtikelInVoorraad {
    private boolean inKorting;
    private LocalDate kortingTotDatum;
    private int kortingTotRest;
    private BigDecimal prijsInKorting;
    private BigDecimal maximaleKorting;
    private int minimaleVoorraad;
    private int normaleVoorraad;
    private int minimaleBestelhoeveelheid;

    public CourantArtikel(Artikel artikel, LocalDate laatsteAankoopdatum,
                          BigDecimal aankoopprijs, BigDecimal minimaleWinstmarge,
                          WinstmargeType winstmargeType, int minimaleVoorraad,
                          int normaleVoorraad, int bestelpunt) {
        super(artikel, laatsteAankoopdatum, aankoopprijs, minimaleWinstmarge, winstmargeType);

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
        if (kortingTotDatum == null) {
            throw new IllegalArgumentException("Korting tot datum mag niet null zijn");
        }
        if (kortingTotDatum.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Korting tot datum moet in de toekomst liggen");
        }
        if (kortingTotRest < 0) {
            throw new IllegalArgumentException("Korting tot rest mag niet negatief zijn");
        }
        if (prijsInKorting == null) {
            throw new IllegalArgumentException("Prijs in korting mag niet null zijn");
        }
        if (prijsInKorting.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Prijs in korting moet positief zijn");
        }
        if (prijsInKorting.compareTo(super.getVerkoopprijs()) >= 0) {
            throw new IllegalArgumentException("Prijs in korting moet lager zijn dan normale verkoopprijs");
        }

        // Controleer of de kortingsprijs niet onder de minimale winstmarge komt
        BigDecimal minimaleVerkoopprijs = getLaatsteAankoopprijs().add(getMinimaleWinstmarge());
        if (prijsInKorting.compareTo(minimaleVerkoopprijs) < 0) {
            throw new IllegalArgumentException("Prijs in korting kan niet lager zijn dan aankoopprijs plus minimale winstmarge");
        }

        this.kortingTotDatum = kortingTotDatum;
        this.kortingTotRest = kortingTotRest;
        this.prijsInKorting = prijsInKorting;
        this.maximaleKorting = super.getVerkoopprijs().subtract(prijsInKorting);
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
}
