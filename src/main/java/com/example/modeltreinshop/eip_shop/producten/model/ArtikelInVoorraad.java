package com.example.modeltreinshop.eip_shop.producten.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ArtikelInVoorraad extends ArtikelDecorator {
    private final PrijsComponent prijsComponent;
    private LocalDate laatsteAankoopdatum;

    public ArtikelInVoorraad(Artikel artikel, LocalDate laatsteAankoopdatum,
                             BigDecimal aankoopprijs, BigDecimal minimaleWinstmarge,
                             WinstmargeType winstmargeType) {
        super(artikel);

        if (laatsteAankoopdatum == null) {
            throw new IllegalArgumentException("Laatste aankoopdatum mag niet null zijn");
        }

        this.prijsComponent = new PrijsComponent(aankoopprijs, minimaleWinstmarge, winstmargeType);
        this.laatsteAankoopdatum = laatsteAankoopdatum;
    }

    public BigDecimal getVerkoopprijs() {
        return prijsComponent.berekenVerkoopprijs();
    }

    public LocalDate getLaatsteAankoopdatum() {
        return laatsteAankoopdatum;
    }

    public void setLaatsteAankoopdatum(LocalDate laatsteAankoopdatum) {
        if (laatsteAankoopdatum == null) {
            throw new IllegalArgumentException("Laatste aankoopdatum mag niet null zijn");
        }
        this.laatsteAankoopdatum = laatsteAankoopdatum;
    }

    public void setAankoopprijs(BigDecimal aankoopprijs) {
        aankoopprijs = aankoopprijs.setScale(2);
        this.prijsComponent.setAankoopprijs(aankoopprijs);
    }

    public BigDecimal getLaatsteAankoopprijs() {
        return prijsComponent.getAankoopprijs();
    }

    public BigDecimal getMinimaleWinstmarge() {
        return prijsComponent.getMinimaleWinstmarge();
    }

    public WinstmargeType getWinstmargeType() {
        return prijsComponent.getWinstmargeType();
    }

    public PrijsComponent getPrijsComponent() {
        return prijsComponent;
    }
}
