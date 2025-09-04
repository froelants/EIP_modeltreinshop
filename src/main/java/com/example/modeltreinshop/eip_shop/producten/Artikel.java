package com.example.modeltreinshop.eip_shop.producten;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Artikel {

    @Id
    @Column(length = 255)
    @NotBlank(message = "Artikelnummer mag niet leeg zijn")
    private String artikelnummer;

    @Column(length = 255)
    @NotBlank(message = "Naam mag niet leeg zijn")
    private String naam;

    @Column(length = 255)
    @NotBlank(message = "Merk mag niet leeg zijn")
    private String merk;

    @Lob
    @NotBlank(message = "Omschrijving mag niet leeg zijn")
    private String omschrijving;

    @Embedded
    private PrijsComponent prijsComponent;

    @Column
    private BigDecimal kortingsPrijs;

    @Column
    private LocalDate kortingTotDatum;

    @ElementCollection
    @CollectionTable(name = "artikel_afbeeldingen", joinColumns = @JoinColumn(name = "artikelnummer"))
    @Column(name = "afbeelding_url")
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
        this.artikelnummer = artikelnummer;
        this.naam = naam;
        this.merk = merk;
        this.omschrijving = omschrijving;
        this.prijsComponent = PrijsComponentFactory.createPrijsComponent(
                gratisArtikel, aankoopprijs, winstmarge, winstmargeType);
        this.prijsComponent.setVerkoopprijs(verkoopprijs);
        this.afbeeldingen = new ArrayList<>(afbeeldingen);
    }
    // No-arg constructor for JPA
    public Artikel() {
        this.afbeeldingen = new ArrayList<>();
    }

    // Getters and setters (omitted for brevity, assume they exist)

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
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

    public void setKorting(BigDecimal kortingsPrijs, @NotNull LocalDate kortingTotDatum) {
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
        return "Artikel{" +
               "artikelnummer='" + artikelnummer + '\'' +
               ", naam='" + naam + '\'' +
               ", merk='" + merk + '\'' +
               ", omschrijving='" + omschrijving + '\'' +
               ", prijsComponent=" + prijsComponent +
               ", kortingsPrijs=" + kortingsPrijs +
               ", kortingTotDatum=" + kortingTotDatum +
               ", afbeeldingen=" + afbeeldingen +
               '}';
    }
}