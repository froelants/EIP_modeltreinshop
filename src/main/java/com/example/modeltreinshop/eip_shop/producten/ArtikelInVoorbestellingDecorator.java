package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class ArtikelInVoorbestelling extends ArtikelDecorator {
    private final PrijsComponent prijsComponent;
    private BigDecimal prijsInVoorbestelling;
    private LocalDate inVoorbestellingTotDatum;
    private YearMonth leveringsKwartaal;
    private boolean onbekendeLeveringstijd;

    public ArtikelInVoorbestelling(Artikel basisArtikel,
                                   BigDecimal aankoopprijs,
                                   BigDecimal minimaleWinstmarge,
                                   WinstmargeType winstmargeType,
                                   BigDecimal prijsInVoorbestelling,
                                   LocalDate inVoorbestellingTotDatum,
                                   YearMonth leveringsKwartaal) {
        super(basisArtikel);
        this.prijsComponent = new PrijsComponent(aankoopprijs, minimaleWinstmarge, winstmargeType);
        valideerVoorbestellingsParameters(prijsInVoorbestelling, inVoorbestellingTotDatum, leveringsKwartaal);
        this.prijsInVoorbestelling = prijsInVoorbestelling.setScale(2);
        this.inVoorbestellingTotDatum = inVoorbestellingTotDatum;
        this.leveringsKwartaal = leveringsKwartaal;
        this.onbekendeLeveringstijd = onbekendeLeveringstijd;
        this.inVoorbestelling = true;
        updateVoorbestellingStatus();
    }

    private void valideerVoorbestellingsParameters(BigDecimal prijsInVoorbestelling,
                                                   LocalDate voorbestellingTotDatum,
                                                   YearMonth leveringsKwartaal) {
        if (voorbestellingTotDatum != null && !voorbestellingTotDatum.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Einddatum voorbestelling moet in de toekomst liggen");
        }
        if (leveringsKwartaal != null && !leveringsKwartaal.isAfter(YearMonth.now())) {
            throw new IllegalArgumentException("Leveringskwartaal moet in de toekomst liggen");
        }
        if (prijsInVoorbestelling != null && prijsInVoorbestelling.compareTo(getLaatsteAankoopprijs()) <= 0) {
            throw new IllegalArgumentException("Prijs in voorbestelling moet groter zijn dan de aankoopprijs");
        }
    }

    private BigDecimal getLaatsteAankoopprijs() {
        return prijsComponent.getAankoopprijs();
    }

    @Override
    public BigDecimal getVerkoopprijs() {
        updateVoorbestellingStatus();
        return inVoorbestelling ? prijsInVoorbestelling : prijsComponent.berekenVerkoopprijs();
    }

    public boolean isInVoorbestelling() {
        updateVoorbestellingStatus();
        return inVoorbestelling;
    }

    private void updateVoorbestellingStatus() {
        if (inVoorbestelling && inVoorbestellingTotDatum.isBefore(LocalDate.now())) {
            stopVoorbestelling();
        }
    }

    public void stopVoorbestelling() {
        this.inVoorbestelling = false;
        this.prijsInVoorbestelling = null;
        this.inVoorbestellingTotDatum = null;
        this.leveringsKwartaal = null;
    }

    public void startVoorbestelling(BigDecimal prijsInVoorbestelling,
                                    LocalDate inVoorbestellingTotDatum, YearMonth leveringsKwartaal) {
        valideerVoorbestellingsParameters(prijsInVoorbestelling,
                                          inVoorbestellingTotDatum, leveringsKwartaal);

        this.prijsInVoorbestelling = prijsInVoorbestelling.setScale(2);
        this.inVoorbestellingTotDatum = inVoorbestellingTotDatum;
        this.leveringsKwartaal = leveringsKwartaal;
        this.inVoorbestelling = true;
    }

    public BigDecimal getPrijsInVoorbestelling() {
        updateVoorbestellingStatus();
        return inVoorbestelling ? prijsInVoorbestelling : null;
    }

    public LocalDate getInVoorbestellingTotDatum() {
        updateVoorbestellingStatus();
        return inVoorbestelling ? inVoorbestellingTotDatum : null;
    }

    public YearMonth getLeveringsKwartaal() {
        updateVoorbestellingStatus();
        return inVoorbestelling ? leveringsKwartaal : null;
    }

    public int getLeveringsKwartaalNummer() {
        updateVoorbestellingStatus();
        return inVoorbestelling ? ((leveringsKwartaal.getMonthValue() - 1) / 3) + 1 : 0;
    }

    public boolean heeftOnbekendeLeveringstijd() {
        return onbekendeLeveringstijd;
    }

    public void setOnbekendeLeveringstijd(boolean onbekendeLeveringstijd) {
        this.onbekendeLeveringstijd = onbekendeLeveringstijd;
    }

    @Override
    public String toString() {
        updateVoorbestellingStatus();
        if (!inVoorbestelling) {
            return super.toString();
        }
        return super.toString() + " (In voorbestelling tot " + inVoorbestellingTotDatum +
               ", prijs: " + prijsInVoorbestelling +
               ", leverbaar in Q" + getLeveringsKwartaalNummer() +
               " " + leveringsKwartaal.getYear() + ")";
    }
}