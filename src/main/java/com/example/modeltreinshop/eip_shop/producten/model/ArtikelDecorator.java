package com.example.modeltreinshop.eip_shop.producten.model;

import java.util.Collection;

public abstract class ArtikelDecorator implements Artikel {
    private final Artikel gedecoreerdArtikel;

    protected ArtikelDecorator(Artikel artikel) {
        if (artikel == null) {
            throw new IllegalArgumentException("Artikel mag niet null zijn.");
        }
        this.gedecoreerdArtikel = artikel;
    }

    @Override
    public final long getId() {
        return gedecoreerdArtikel.getId();
    }

    @Override
    public final String getNummer() {
        return gedecoreerdArtikel.getNummer();
    }

    @Override
    public final String getNaam() {
        return gedecoreerdArtikel.getNaam();
    }

    @Override
    public final String getOmschrijving() {
        return gedecoreerdArtikel.getOmschrijving();
    }

    @Override
    public final String getMerk() {
        return gedecoreerdArtikel.getMerk();
    }
    @Override
    public final void setNaam(String naam) {
        gedecoreerdArtikel.setNaam(naam);
    }

    @Override
    public final void setOmschrijving(String omschrijving) {
        gedecoreerdArtikel.setOmschrijving(omschrijving);
    }

    @Override
    public final void setMerk(String merk) {
        gedecoreerdArtikel.setMerk(merk);
    }

    public final boolean isEenmaligProduct(){
        return gedecoreerdArtikel.isEenmaligProduct();
    }

    public final boolean addAfbeelding(String afbeelding){
        return gedecoreerdArtikel.addAfbeelding(afbeelding);
    }

    public final boolean removeAfbeelding(String afbeelding){
        return gedecoreerdArtikel.removeAfbeelding(afbeelding);
    }

    public final Collection<String> getAfbeeldingen() {
        return gedecoreerdArtikel.getAfbeeldingen();
    }
}
