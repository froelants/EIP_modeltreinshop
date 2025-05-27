package com.example.modeltreinshop.eip_shop.producten.model;

import java.time.LocalDate;

public class ArtikelInBackorder extends ArtikelDecorator {
    private final LocalDate besteldatum;
    private final int aantalInBackorder;
    private final int verwachteLeveringstijdInDagen;
    private final ArtikelInVoorraad artikelInVoorraad;

    public ArtikelInBackorder(ArtikelInVoorraad artikel, LocalDate besteldatum,
                              int aantalInBackorder, int verwachteLeveringstijdInDagen) {
        super(artikel);

        if (artikel.isEenmaligProduct()) {
            throw new IllegalArgumentException("Eenmalige artikelen kunnen niet in backorder");
        }
        if (artikel.getClass().equals(ArtikelInVoorbestelling.class)) {
            throw new IllegalArgumentException("Artikelen in voorbestelling kunnen niet in backorder");
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

        this.artikelInVoorraad = artikel;
        this.besteldatum = besteldatum;
        this.aantalInBackorder = aantalInBackorder;
        this.verwachteLeveringstijdInDagen = verwachteLeveringstijdInDagen;
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
