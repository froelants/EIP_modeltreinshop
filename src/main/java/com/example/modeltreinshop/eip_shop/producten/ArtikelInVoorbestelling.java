package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/*
 2025/05/27 12:00
 */
/**
 * ArtikelInVoorbestelling vertegenwoordigt een artikel dat kan worden voorbesteld.
 *
 * Business Logic:
 * 1. Een artikel kan een leveringstijd (kwartaal) hebben of niet:
 *    - Met leveringstijd: heeft voorbestelprijs en voorschot
 *    - Zonder leveringstijd: geen voorschot mogelijk (altijd 0 euro)
 *
 * 2. Voorbestelling is mogelijk:
 *    - Tot de voorbestellingTotDatum
 *    - Zolang het maximaal aantal voorbestellingen niet is bereikt (indien ingesteld)
 *
 * 3. Na voorbestellingTotDatum maar voor levering:
 *    - Artikel kan nog gereserveerd worden
 *    - Niet meer tegen voorbestelprijs
 *    - Wordt dan een ArtikelInVoorraad (conversie gebeurt buiten deze class)
 *    - Voorbestellingsprijs wordt de verkoopprijs
 *    - Voorbestelling aan de voorbestelPrijs is mogelijk tot en met de voorbestellingTotDatum
 *
 * 4. Voorschot:
 *    - Alleen mogelijk bij artikelen met leveringskwartaal
 *    - Voorgestelde waarde kan later aangepast worden
 *    - Bij artikelen zonder leveringskwartaal altijd 0 euro
 *
 * 5. Aantal in voorbestelling:
 *    - Start op 0
 *    - Wordt later ingesteld (aantal voorbestellingen + reserve)
 *    - Bijhouden van voorbestellingen gebeurt buiten deze class
 *    - Voorbestellingen zonder leveringskwartaal kunnen door de klant worden ongedaangemaakt (wordt buiten deze class afgehandeld.)
 *    - Maximum aantal artikelen in is optioneel
 */
public class ArtikelInVoorbestelling extends Artikel {
    private final LocalDate besteldatum;
    private BigDecimal voorbestellingsPrijs;
    private LocalDate voorbestellingTotDatum;
    private YearMonth leveringsKwartaal;
    private BigDecimal voorschot;
    private boolean zonderLeveringstijd;
    private int aantalInVoorbestelling;
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
        super(artikelnummer, naam, merk, omschrijving, gratisArtikel,
              aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
              afbeeldingen);

        List<String> errors = validateVoorbestellingParameters(
                besteldatum, voorbestellingsPrijs, voorbestellingTotDatum,
                leveringsKwartaal, voorschot, zonderLeveringstijd
                                                              );

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }

        this.besteldatum = besteldatum;
        this.voorbestellingsPrijs = voorbestellingsPrijs;
        this.voorbestellingTotDatum = voorbestellingTotDatum;
        this.leveringsKwartaal = leveringsKwartaal;
        this.voorschot = zonderLeveringstijd ? BigDecimal.ZERO : voorschot;
        this.zonderLeveringstijd = zonderLeveringstijd;
        this.aantalInVoorbestelling = 0;
        this.maximaalAantalVoorbestellingen = null;
    }

    private List<String> validateVoorbestellingParameters(
            LocalDate besteldatum,
            BigDecimal voorbestellingsPrijs,
            LocalDate voorbestellingTotDatum,
            YearMonth leveringsKwartaal,
            BigDecimal voorschot,
            boolean zonderLeveringstijd) {

        List<String> errors = new ArrayList<>();

        if (besteldatum == null) {
            errors.add("Besteldatum mag niet null zijn");
        }
        if (voorbestellingsPrijs == null || voorbestellingsPrijs.compareTo(getMinimaleVerkoopprijs()) < 0) {
            errors.add("Voorbestellingsprijs moet hoger zijn dan minimale verkoopprijs");
        }
        if (voorbestellingTotDatum == null ||
            (besteldatum != null && voorbestellingTotDatum.isBefore(besteldatum))) {
            errors.add("Voorbestelling einddatum moet na besteldatum liggen");
        }
        if (!zonderLeveringstijd && leveringsKwartaal == null) {
            errors.add("Leveringskwartaal is verplicht als er een leveringstijd is");
        }
        if (voorschot == null || voorschot.compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Voorschot mag niet negatief zijn");
        }

        return errors;
    }

    @Override
    public BigDecimal getVerkoopprijs() {
        return isVoorbestellingMogelijk() ? voorbestellingsPrijs : super.getVerkoopprijs();
    }

    public boolean isVoorbestellingMogelijk() {
        return LocalDate.now().isBefore(voorbestellingTotDatum) &&
               (maximaalAantalVoorbestellingen == null ||
                aantalInVoorbestelling < maximaalAantalVoorbestellingen);
    }

    // Getters
    public LocalDate getBesteldatum() { return besteldatum; }
    public BigDecimal getVoorbestellingsPrijs() { return voorbestellingsPrijs; }
    public LocalDate getVoorbestellingTotDatum() { return voorbestellingTotDatum; }
    public boolean isZonderLeveringstijd() { return zonderLeveringstijd; }
    public YearMonth getLeveringsKwartaal() {
        return zonderLeveringstijd ? null : leveringsKwartaal;
    }
    public BigDecimal getVoorschot() { return voorschot; }
    public int getAantalInVoorbestelling() { return aantalInVoorbestelling; }
    public Integer getMaximaalAantalVoorbestellingen() { return maximaalAantalVoorbestellingen; }

    // Setters
    public void setVoorbestellingTotDatum(LocalDate voorbestellingTotDatum) {
        if (voorbestellingTotDatum == null) {
            throw new IllegalArgumentException("Voorbestellingsdatum mag niet null zijn");
        }
        if (voorbestellingTotDatum.isBefore(besteldatum)) {
            throw new IllegalArgumentException("Voorbestellingsdatum moet na besteldatum liggen");
        }
        this.voorbestellingTotDatum = voorbestellingTotDatum;
    }

    public void setVoorbestellingsPrijs(BigDecimal voorbestellingsPrijs) {
        if (voorbestellingsPrijs == null ||
            voorbestellingsPrijs.compareTo(getMinimaleVerkoopprijs()) < 0) {
            throw new IllegalArgumentException("Voorbestellingsprijs moet hoger zijn dan minimale verkoopprijs");
        }
        this.voorbestellingsPrijs = voorbestellingsPrijs;
    }

    public void setVoorschot(BigDecimal voorschot) {
        if (zonderLeveringstijd) {
            this.voorschot = BigDecimal.ZERO;
        } else {
            if (voorschot == null || voorschot.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Voorschot mag niet negatief zijn");
            }
            this.voorschot = voorschot;
        }
    }

    public void setAantalInVoorbestelling(int aantal) {
        if (aantal < 0) {
            throw new IllegalArgumentException("Aantal in voorbestelling mag niet negatief zijn");
        }
        this.aantalInVoorbestelling = aantal;
    }

    public void setMaximaalAantalVoorbestellingen(Integer maximum) {
        if (maximum != null && maximum <= 0) {
            throw new IllegalArgumentException("Maximaal aantal voorbestellingen moet positief zijn");
        }
        this.maximaalAantalVoorbestellingen = maximum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\nVoorbestelling details:");
        sb.append(String.format("\n  Besteldatum: %s", besteldatum));
        sb.append(String.format("\n  Voorbestellingsprijs: € %.2f", voorbestellingsPrijs));
        sb.append(String.format("\n  Voorbestelling mogelijk tot: %s", voorbestellingTotDatum));
        sb.append(String.format("\n  Aantal in voorbestelling: %d", aantalInVoorbestelling));
        if (maximaalAantalVoorbestellingen != null) {
            sb.append(String.format("\n  Maximum aantal: %d", maximaalAantalVoorbestellingen));
        }
        if (zonderLeveringstijd) {
            sb.append("\n  Zonder leveringstijd");
            sb.append("\n  Voorschot: € 0.00");
        } else {
            sb.append(String.format("\n  Verwachte levering: %s", leveringsKwartaal));
            sb.append(String.format("\n  Voorschot: € %.2f", voorschot));
        }
        return sb.toString();
    }
}