package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 2025/05/27 11:15
 */
public class ArtikelInVoorraad extends Artikel {
    /* ArtikelInVoorraad Class
     * Business Logic:
     * - Represents articles currently in stock
     * - Tracks:
     *   - Minimum stock level
     *   - Current stock level
     *   - Maximum stock level
     * - Validates:
     *   - Stock levels must be positive
     *   - Min stock ≤ Current stock ≤ Max stock
     * - Provides stock status information
     * - Manages stock mutations (add/remove)
     */
    private int voorraad;
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
                             // ArtikelInVoorraad specific parameters:
                             int voorraad) {
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

        if (voorraad < 0) {
            throw new IllegalArgumentException("Voorraad mag niet negatief zijn");
        }
        this.voorraad = voorraad;
        this.laatsteAankoopdatum = LocalDate.now();
    }

    public int getVoorraad() {
        return voorraad;
    }

    public void wijzigVoorraad(int aantal) {
        if (voorraad + aantal < 0) {
            throw new IllegalArgumentException("Resulterende voorraad mag niet negatief zijn");
        }
        voorraad += aantal;
    }

    public LocalDate getLaatsteAankoopdatum() {
        return laatsteAankoopdatum;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());

        sb.append("\nVoorraadgegevens:");
        sb.append(String.format("\n  Huidige voorraad: %d", voorraad));
        sb.append(String.format("\n  Laatste aankoopdatum: %s", laatsteAankoopdatum));

        return sb.toString();
    }

    public void addVoorraad(int voorraad) {//in plaats van setvoorraad, de voorraad wordt bijgevuld in plaats van en een bepaalde waarde gezet.
        if (voorraad < 0) {
            throw new IllegalArgumentException("Voorraad mag niet negatief zijn");
        }
        this.voorraad = voorraad;
    }

    /**
     * Zet de voorraad van het artikel.
     * De voorraad mag alleen worden gezet als de huidige voorraad 0 is.
     * @param voorraad de nieuwe voorraad
     * @throws IllegalArgumentException als de huidige voorraad niet 0 is of de nieuwe voorraad negatief is
     */
    public void setVoorraad(int voorraad) {
        List<String> errors = new ArrayList<>();
        if (this.voorraad != 0) {
            errors.add("De huidige voorraad moet 0 zijn");
        }
        if (voorraad < 0) {
            errors.add("De nieuwe voorraad mag niet negatief zijn");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(" en ", errors));
        }
        this.voorraad = voorraad;
    }
}