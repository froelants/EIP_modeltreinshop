package com.example.modeltreinshop.eip_shop.producten;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "artikel_in_backorder")
@PrimaryKeyJoinColumn(name = "artikelnummer")
public class ArtikelInBackorder extends CourantArtikel {

    @Column
    @NotNull(message = "Besteldatum mag niet null zijn")
    private LocalDate besteldatum;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyColumn(name = "backorder_id")
    private Map<Integer, BackorderLijn> backorders;

    @Column
    private int nextBackorderId;

    public ArtikelInBackorder(String artikelnummer,
                              String naam,
                              String merk,
                              String omschrijving,
                              boolean gratisArtikel,
                              BigDecimal aankoopprijs,
                              BigDecimal winstmarge,
                              WinstmargeType winstmargeType,
                              BigDecimal verkoopprijs,
                              List<String> afbeeldingen,
                              int voorraad,
                              int minimaleVoorraad,
                              int normaleVoorraad,
                              int minimaleBestelhoeveelheid,
                              LocalDate besteldatum) {
        super(artikelnummer, naam, merk, omschrijving, gratisArtikel,
              aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
              afbeeldingen, voorraad, minimaleVoorraad, normaleVoorraad,
              minimaleBestelhoeveelheid);

        this.besteldatum = besteldatum;
        this.backorders = new HashMap<>();
        this.nextBackorderId = 1;
    }

    // No-arg constructor for JPA
    public ArtikelInBackorder() {
        super();
    }

    public int addBackorderLijn(LocalDate verwachteDatum, int aantal) {
        int backorderId = nextBackorderId++;
        backorders.put(backorderId, new BackorderLijn(verwachteDatum, aantal));
        return backorderId;
    }

    public void removeBackorderLijn(int backorderId) {
        if (!backorders.containsKey(backorderId)) {
            throw new IllegalArgumentException("Backorder ID bestaat niet");
        }
        backorders.remove(backorderId);
    }

    public int getAantalInBackorder() {
        return backorders.values().stream()
                         .mapToInt(BackorderLijn::getAantal)
                         .sum();
    }

    public LocalDate getBesteldatum() {
        return besteldatum;
    }

    public Map<Integer, BackorderLijn> getBackorders() {
        return Collections.unmodifiableMap(backorders);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\nBackordergegevens:");
        sb.append(String.format("\n  Besteldatum: %s", besteldatum));
        backorders.forEach((id, lijn) ->
                                   sb.append(String.format("\n  ID %d: %d artikelen verwacht op %s",
                                                           id, lijn.getAantal(), lijn.getVerwachteDatum())));
        return sb.toString();
    }
}