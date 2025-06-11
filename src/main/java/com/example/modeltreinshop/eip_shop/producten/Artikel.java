package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public abstract class Artikel {
    /* Artikel Class
     * Business Logic:
     * - Base class for all article types in the shop
     * - Contains common attributes: artikelnummer, naam, merk, omschrijving, etc.
     * - Tracks gratis artikelen (free articles)
     * - Manages pricing:
     *   - Aankoopprijs (purchase price)
     *   - Winstmarge (profit margin)
     *   - Verkoopprijs (sales price)
     * - Uses WinstmargeStrategy for profit calculation
     * - Maintains list of product images
     * - Validates:
     *   - Required fields cannot be null/empty
     *   - Prices must be positive
     *   - Artikelnummer must follow specific format
     */
    private final String artikelnummer;
    private final String naam;
    private final String merk;
    private String omschrijving;
    private final PrijsComponent prijsComponent;
    private BigDecimal kortingsPrijs;
    private LocalDate kortingTotDatum;
    private final List<String> afbeeldingen;

    public Artikel(String artikelnummer,
                   String naam,
                   String merk,
                   String omschrijving,
                   boolean gratisArtikel,
                   BigDecimal aankoopprijs,
                   BigDecimal winstmarge,
                   WinstmargeType winstmargeType,
                   BigDecimal verkoopprijs,
                   List<String> afbeeldingen) {

        validateNotNull(artikelnummer, naam, merk, omschrijving);
        validateNotBlank(artikelnummer, naam, merk, omschrijving);

        this.artikelnummer = artikelnummer.trim();
        this.naam = naam.trim();
        this.merk = merk.trim();
        this.omschrijving = omschrijving.trim();
        this.prijsComponent = PrijsComponentFactory.createPrijsComponent(
                gratisArtikel, aankoopprijs, winstmarge, winstmargeType);
        this.prijsComponent.setVerkoopprijs(verkoopprijs);
        this.afbeeldingen = new ArrayList<>(afbeeldingen);
    }

    private void validateNotNull(String... fields) {
        for (String field : fields) {
            if (field == null) {
                throw new IllegalArgumentException("Verplicht veld mag niet null zijn");
            }
        }
    }

    private void validateNotBlank(String... fields) {
        for (String field : fields) {
            if (field.trim().isBlank()) {
                throw new IllegalArgumentException("Verplicht veld mag niet leeg zijn");
            }
        }
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        if (omschrijving == null || omschrijving.trim().isBlank()) {
            throw new IllegalArgumentException("Omschrijving mag niet leeg zijn");
        }
        this.omschrijving = omschrijving.trim();
    }

    public String getMerk() {
        return merk;
    }

    public String getArtikelnummer() {
        return artikelnummer;
    }

    public PrijsComponent getPrijsComponent() {
        return prijsComponent;
    }

    public BigDecimal getKortingsPrijs() {
        return kortingsPrijs;
    }

    public void setKortingsPrijs(BigDecimal kortingsPrijs) {
        this.kortingsPrijs = kortingsPrijs;
    }

    public LocalDate getKortingTotDatum() {
        return kortingTotDatum;
    }

    public void setKortingTotDatum(LocalDate kortingTotDatum) {
        this.kortingTotDatum = kortingTotDatum;
    }

    public void setVerkoopprijs(BigDecimal verkoopprijs) {
        prijsComponent.setVerkoopprijs(verkoopprijs);
    }

    public BigDecimal getVerkoopprijs() {
        if (isInKorting()) {
            return kortingsPrijs;
        }
        return prijsComponent.getVerkoopprijs();
    }

    public boolean isInKorting() {
        return kortingsPrijs != null && kortingTotDatum != null &&
               !LocalDate.now().isAfter(kortingTotDatum);
    }

    public void setKorting(BigDecimal kortingsPrijs, LocalDate kortingTotDatum) {
        if (kortingTotDatum == null) {
            throw new IllegalArgumentException("Kortingsdatum mag niet null zijn");
        }
        if (kortingsPrijs.compareTo(prijsComponent.getAankoopprijs()) <= 0) {
            throw new IllegalArgumentException("Kortingsprijs moet hoger zijn dan aankoopprijs");
        }

        this.kortingsPrijs = kortingsPrijs;
        this.kortingTotDatum = kortingTotDatum;
    }

    public void stopKorting() {
        this.kortingsPrijs = null;
        this.kortingTotDatum = null;
    }

    public String getNaam() {
        return naam;
    }

    protected BigDecimal getAankoopprijs() {
        return prijsComponent.getAankoopprijs();
    }


    public BigDecimal getMinimaleVerkoopprijs() {
        if (isInKorting()) {
            return kortingsPrijs;
        }
        return prijsComponent.getMinimaleVerkoopprijs();
    }

    public final boolean addAfbeelding(String afbeelding) {
        if (afbeelding == null || afbeelding.trim().isBlank()) {
            throw new IllegalArgumentException("Afbeelding mag niet leeg zijn");
        }
        return this.afbeeldingen.add(afbeelding.trim());
    }

    public final boolean removeAfbeelding(String afbeelding) {
        if (afbeelding == null || afbeelding.trim().isBlank()) {
            throw new IllegalArgumentException("Afbeelding mag niet leeg zijn");
        }
        if (!this.afbeeldingen.contains(afbeelding)) {
            throw new IllegalArgumentException("Afbeelding bestaat niet in Artikel");
        }
        return this.afbeeldingen.remove(afbeelding);
    }

    public final Collection<String> getAfbeeldingen() {
        return Collections.unmodifiableList(afbeeldingen);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Artikel artikel)) {
            return false;
        }
        return (this.artikelnummer.equals(artikel.artikelnummer)) &&
               (this.naam.equals(artikel.naam)) &&
               (this.merk.equals(artikel.merk));
    }

    public int hashCode() {
        return artikelnummer.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s [%s] %s %s",
                                getClass().getSimpleName(),
                                artikelnummer,
                                merk,
                                naam));

        sb.append(String.format(" - € %.2f", getVerkoopprijs()));
        if (isInKorting()) {
            sb.append(String.format(" (in korting: € %.2f tot %s)",
                                    kortingsPrijs,
                                    kortingTotDatum));
        }
        if (prijsComponent.isGratisArtikel()) {
            sb.append(" (gratis artikel)");
        }
        sb.append(String.format(" - %s", omschrijving));

        if (!afbeeldingen.isEmpty()) {
            sb.append(String.format(" - %d afbeelding(en)\n", afbeeldingen.size()));
            sb.append("Afbeeldingen:\n");
            for (String url : afbeeldingen) {
                sb.append("  ").append(url).append("\n");
            }
            if (sb.charAt(sb.length() - 1) == '\n') {
                sb.setLength(sb.length() - 1);
            }
        }
        return sb.toString();
    }
}