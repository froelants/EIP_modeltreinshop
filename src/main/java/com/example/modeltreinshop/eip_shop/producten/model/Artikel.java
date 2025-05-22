package com.example.modeltreinshop.eip_shop.producten;

import java.io.Serializable;
import java.util.Collection;

public interface Artikel extends Serializable {
    long getId();
    String getNummer();
    String getNaam();
    void setNaam(String naam);
    String getOmschrijving();
    void setOmschrijving(String omschrijving);
    String getMerk();
    void setMerk(String merk);
    boolean isEenmaligProduct();
    boolean addAfbeelding(String afbeelding);
    boolean removeAfbeelding(String afbeelding);
    Collection<String> getAfbeeldingen();
}
