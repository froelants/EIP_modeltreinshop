package com.example.modeltreinshop.eip_shop.producten;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Entity
@Table(name = "artikel_in_voorbestelling")
@PrimaryKeyJoinColumn(name = "artikelnummer")
public class ArtikelInVoorbestelling extends Artikel {

    @Column
    @NotNull(message = "Besteldatum mag niet null zijn")
    private LocalDate besteldatum;

    @Column
    @NotNull(message = "Voorbestellingsprijs mag niet null zijn")
    private BigDecimal voorbestellingsPrijs;

    @Column
    @NotNull(message = "Voorbestelling tot datum mag niet null zijn")
    private LocalDate voorbestellingTotDatum;

    @Column
    private YearMonth leveringsKwartaal;

    @Column
    @PositiveOrZero(message = "Voorschot mag niet negatief zijn")
    private BigDecimal voorschot;

    @Column
    private boolean zonderLeveringstijd;

    @Column
    @PositiveOrZero(message = "Aantal in voorbestelling mag niet negatief zijn")
    private int aantalInVoorbestelling;

    @Column
    private Integer maximaalAantalVoorbestellingen;

    public ArtikelInVoorbestelling(String artikelnummer,
                                   String naam,
                                   String merk,
                                   String omschrijving,
                                   boolean gratisArtikel,
                                   BigDecimal aankoopprijs,
                                   BigDecimal winstmarge,
                                   WinstmargeType winstmargeType,
                                   BigDecimal verkoopprijs,
                                   List<String> afbeeldingen,
                                   LocalDate besteldatum,
                                   BigDecimal voorbestellingsPrijs,
                                   LocalDate voorbestellingTotDatum,
                                   YearMonth leveringsKwartaal,
                                   BigDecimal voorschot,
                                   boolean zonderLeveringstijd) {
        super(artikelnummer,
              naam,
              merk,
              omschrijving,
              gratisArtikel,
              aankoopprijs,
              winstmarge,
              winstmargeType,
              verkoopprijs,
              afbeeldingen);

        this.besteldatum = besteldatum;
        this.voorbestellingsPrijs = voorbestellingsPrijs;
        this.voorbestellingTotDatum = voorbestellingTotDatum;
        this.leveringsKwartaal = leveringsKwartaal;
        this.voorschot = zonderLeveringstijd ? BigDecimal.ZERO : voorschot;
        this.zonderLeveringstijd = zonderLeveringstijd;
        this.aantalInVoorbestelling = 0;
        this.maximaalAantalVoorbestellingen = null;
    }

    // No-arg constructor for JPA
    public ArtikelInVoorbestelling() {
        super();
    }

    // Getters and setters (omitted for brevity, assume they exist)

    public boolean isVoorbestellingMogelijk() {
        if (maximaalAantalVoorbestellingen != null &&
            aantalInVoorbestelling >= maximaalAantalVoorbestellingen) {
            return false;
        }
        return LocalDate.now().isBefore(voorbestellingTotDatum);
    }

    @Override
    public BigDecimal getVerkoopprijs() {
        if (LocalDate.now().isBefore(voorbestellingTotDatum) && isVoorbestellingMogelijk()) {
            return voorbestellingsPrijs;
        }
        return super.getVerkoopprijs();
    }

    public LocalDate getBesteldatum() {
        return besteldatum;
    }

    public BigDecimal getVoorbestellingsPrijs() {
        return voorbestellingsPrijs;
    }

    public LocalDate getVoorbestellingTotDatum() {
        return voorbestellingTotDatum;
    }

    public YearMonth getLeveringsKwartaal() {
        return zonderLeveringstijd ? null : leveringsKwartaal;
    }

    public BigDecimal getVoorschot() {
        return voorschot;
    }

    public boolean isZonderLeveringstijd() {
        return zonderLeveringstijd;
    }

    public int getAantalInVoorbestelling() {
        return aantalInVoorbestelling;
    }

    public Integer getMaximaalAantalVoorbestellingen() {
        return maximaalAantalVoorbestellingen;
    }

    public void setVoorbestellingsPrijs(BigDecimal voorbestellingsPrijs) {
        this.voorbestellingsPrijs = voorbestellingsPrijs;
    }

    public void setVoorbestellingTotDatum(LocalDate voorbestellingTotDatum) {
        this.voorbestellingTotDatum = voorbestellingTotDatum;
    }

    public void setLeveringsKwartaal(YearMonth leveringsKwartaal) {
        this.leveringsKwartaal = leveringsKwartaal;
    }

    public void setVoorschot(BigDecimal voorschot) {
        this.voorschot = voorschot;
    }

    public void setAantalInVoorbestelling(int aantal) {
        this.aantalInVoorbestelling = aantal;
    }

    public void setMaximaalAantalVoorbestellingen(Integer maximum) {
        this.maximaalAantalVoorbestellingen = maximum;
    }
}