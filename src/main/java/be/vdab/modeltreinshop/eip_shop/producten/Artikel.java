package be.vdab.modeltreinshop.eip_shop.producten;

import java.io.Serializable;

public interface Artikel extends Serializable {
    long getId();
    String getNummer();
    String getNaam();
    void setNaam(String naam);
    String getOmschrijving();
    void setOmschrijving(String omschrijving);
    String getMerk();
    void setMerk(String merk);
}
