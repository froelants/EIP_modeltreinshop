package com.example.modeltreinshop.eip_shop.klanten;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
public class Adres {

    @Column(name = "straat")
    @NotBlank(message = "Straat mag niet leeg zijn")
    private String straat;

    @Column(name = "huisnummer")
    @NotBlank(message = "Huisnummer mag niet leeg zijn")
    private String huisnummer;

    @Column(name = "busnummer")
    private String busnummer;

    @Column(name = "postcode")
    @NotBlank(message = "Postcode mag niet leeg zijn")
    private String postcode;

    @Column(name = "gemeente")
    @NotBlank(message = "Gemeente mag niet leeg zijn")
    private String gemeente;

    @ManyToOne
    @NotNull(message = "Land mag niet null zijn")
    private Land land;

    public Adres() {}

    public Adres(String straat, String huisnummer, String busnummer, String postcode, String gemeente, Land land) {
        this.straat = straat;
        this.huisnummer = huisnummer;
        this.busnummer = busnummer;
        this.postcode = postcode;
        this.gemeente = gemeente;
        this.land = land;
    }

    // Getters en setters
    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getBusnummer() {
        return busnummer;
    }

    public void setBusnummer(String busnummer) {
        this.busnummer = busnummer;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adres adres = (Adres) o;
        return Objects.equals(straat, adres.straat) &&
               Objects.equals(huisnummer, adres.huisnummer) &&
               Objects.equals(busnummer, adres.busnummer) &&
               Objects.equals(postcode, adres.postcode) &&
               Objects.equals(gemeente, adres.gemeente) &&
               Objects.equals(land, adres.land);
    }

    @Override
    public int hashCode() {
        return Objects.hash(straat, huisnummer, busnummer, postcode, gemeente, land);
    }
}