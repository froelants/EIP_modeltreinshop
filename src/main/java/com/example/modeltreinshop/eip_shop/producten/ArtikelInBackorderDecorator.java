package com.example.modeltreinshop.eip_shop.producten;

import java.time.LocalDate;

public class ArtikelInBackorder extends extends ArtikelDecorator {
    private final LocalDate besteldatum;
    private final int aantalInBackorder;
    private final int verwachteLeveringstijdInDagen;


    public ArtikelInBackorderDecorator(CourantArtikelDecorator basisArtikel, LocalDate besteldatum,
                                       int aantalInBackorder, int verwachteLeveringstijdInDagen) {
        super(basisArtikel);
        if (!(basisArtikel instanceof CourantArtikelDecorator)) {
            throw new IllegalArgumentException("Alleen CourantArtikelen kunnen in backorder");
        }
        if (besteldatum == null) {
            throw new IllegalArgumentException("Besteldatum mag niet null zijn");
        }
        if (aantalInBackorder <= 0) {
            throw new IllegalArgumentException("Aantal in backorder moet groter zijn dan 0");
        }
        if (verwachteLeveringstijdInDagen <= 0) {
            throw new IllegalArgumentException("Verwachte leveringstijd moet groter zijn dan 0");
        }
        this.besteldatum = besteldatum;
        this.aantalInBackorder = aantalInBackorder;
        this.verwachteLeveringstijdInDagen = verwachteLeveringstijdInDagen;
    }

    private boolean isOrContainsVoorbestelling(Artikel artikel) {
        if (artikel instanceof ArtikelInVoorbestellingDecorator) {
            return true;
        }
        if (artikel instanceof ArtikelDecorator) {
            return isOrContainsVoorbestelling(((ArtikelDecorator) artikel).getGedecoreerdArtikel());
        }
        return false;
    }

    public LocalDate getBesteldatum() {
        return besteldatum;
    }

    public int getAantalInBackorder() {
        return aantalInBackorder;
    }

    public int getVerwachteLeveringstijdInDagen() {
        return verwachteLeveringstijdInDagen;
    }

    public LocalDate berekenVerwachteLeveringsdatum() {
        LocalDate verwachteDatum = besteldatum.plusDays(verwachteLeveringstijdInDagen);
        artikelInVoorraad.setLaatsteAankoopdatum(verwachteDatum);
        return verwachteDatum;
    }
}