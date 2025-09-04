package com.example.modeltreinshop.eip_shop.producten;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "artikel_in_voorraad")
@PrimaryKeyJoinColumn(name = "artikelnummer")
public class ArtikelInVoorraad extends Artikel {

    @Column
    @PositiveOrZero(message = "Voorraad mag niet negatief zijn")
    private int voorraad;

    @Column
    private LocalDate laatsteAankoopdatum;

    public ArtikelInVoorraad(String artikelnummer,
                             String naam,
                             String merk,
                             String omschrijving,
                             boolean gratisArtikel,
                             BigDecimal aankoopprijs,
                             BigDecimal winstmarge,
                             WinstmargeType winstmargeType,
                             BigDecimal verkoopprijs,
                             List<String> afbeeldingen,
                             int voorraad) {
        super(artikelnummer, naam, merk, omschrijving, gratisArtikel,
              aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
              afbeeldingen);

        this.voorraad = voorraad;
        this.laatsteAankoopdatum = LocalDate.now();
    }

    // No-arg constructor for JPA
    public ArtikelInVoorraad() {
        super();
    }

    public int getVoorraad() {
        return voorraad;
    }

    public void wijzigVoorraad(int aantal) {
        this.voorraad += aantal;
    }

    public LocalDate getLaatsteAankoopdatum() {
        return laatsteAankoopdatum;
    }

    @Override
    public String toString() {
        return "ArtikelInVoorraad{" +
               "voorraad=" + voorraad +
               ", laatsteAankoopdatum=" + laatsteAankoopdatum +
               '}';
    }

    public void setVoorraad(int voorraad) {
        this.voorraad = voorraad;
    }
}