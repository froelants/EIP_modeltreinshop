package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class ArtikelInBackorder extends CourantArtikel {
    /* ArtikelInBackorder Class
     * Business Logic:
     * - Represents articles that are temporarily out of stock
     * - Maintains list of BackorderLijn entries
     * - Each BackorderLijn contains:
     *   - Expected delivery date
     *   - Number of items expected
     * - Total backorder amount is sum of all BackorderLijn amounts
     * - Can add new backorder lines with validation
     * - Tracks when items will be back in stock
     */
    private final LocalDate besteldatum;
    private final Map<Integer, BackorderLijn> backorders;
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

        if (besteldatum == null) {
            throw new IllegalArgumentException("Besteldatum mag niet null zijn");
        }

        this.besteldatum = besteldatum;
        this.backorders = new HashMap<>();
        this.nextBackorderId = 1;
    }

    public int addBackorderLijn(LocalDate verwachteDatum, int aantal) {
        if (aantal <= 0) {
            throw new IllegalArgumentException("Aantal moet positief zijn");
        }
        if (verwachteDatum == null || verwachteDatum.isBefore(besteldatum)) {
            throw new IllegalArgumentException("Verwachte datum moet na besteldatum liggen");
        }

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
