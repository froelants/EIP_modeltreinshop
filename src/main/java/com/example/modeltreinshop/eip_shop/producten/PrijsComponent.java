package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PrijsComponent {
    /* PrijsComponent Class
     * Business Logic:
     * - Represents a price component in pricing calculation
     * - Can be:
     *   - Base price
     *   - Profit margin
     *   - Discounts
     *   - Additional costs
     * - Maintains amount and type
     * - Used in price calculation chain
     */
    private BigDecimal aankoopprijs;
    private BigDecimal minimaleWinstmarge;
    private WinstmargeType winstmargeType;
    private BigDecimal verkoopprijs;
    private boolean gratisArtikel = false; //expliciete initialisatie om misverstanden te voorkomen.

    public PrijsComponent(BigDecimal aankoopprijs,
                          BigDecimal minimaleWinstmarge,
                          WinstmargeType winstmargeType) {
        validateNotNull(aankoopprijs, minimaleWinstmarge, winstmargeType);
        validateParameters(aankoopprijs, minimaleWinstmarge);

        this.aankoopprijs = aankoopprijs.setScale(2, RoundingMode.HALF_UP);
        this.minimaleWinstmarge = minimaleWinstmarge.setScale(2, RoundingMode.HALF_UP);
        this.winstmargeType = winstmargeType;
    }

    private void validateNotNull(BigDecimal aankoopprijs,
                                 BigDecimal minimaleWinstmarge,
                                 WinstmargeType type) {
        if (aankoopprijs == null || minimaleWinstmarge == null || type == null) {
            throw new IllegalArgumentException("Prijs parameters mogen niet null zijn");
        }
    }

    protected void validateParameters(BigDecimal aankoopprijs, BigDecimal verkoopprijs) {
        if (aankoopprijs == null || verkoopprijs == null) {
            throw new IllegalArgumentException("Prijzen mogen niet null zijn");
        }
        if (aankoopprijs.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Aankoopprijs moet groter zijn dan of gelijk aan 0");
        }
        if (verkoopprijs.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Verkoopprijs moet groter zijn dan of gelijk aan 0");
        }
    }

    // Special constructor for free articles
    public static PrijsComponent createGratisArtikel() {
        PrijsComponent gratisComponent = new PrijsComponent(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                WinstmargeType.EURO
        );
        gratisComponent.gratisArtikel = true;
        gratisComponent.setVerkoopprijs(BigDecimal.ZERO);
        return gratisComponent;
    }

    public boolean isGratisArtikel() {
        return gratisArtikel;
    }

    public void setVerkoopprijs(BigDecimal verkoopprijs) {
        if (verkoopprijs == null) {
            throw new IllegalArgumentException("Verkoopprijs mag niet null zijn");
        }

        List<String> errors = validateVerkoopprijs(verkoopprijs);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }

        this.verkoopprijs = verkoopprijs.setScale(2, RoundingMode.HALF_UP);
    }

    private List<String> validateVerkoopprijs(BigDecimal verkoopprijs) {
        List<String> errors = new ArrayList<>();

        if (gratisArtikel && verkoopprijs.compareTo(BigDecimal.ZERO) != 0) {
            errors.add("Verkoopprijs moet 0 zijn voor gratis artikelen");
        }

        if (!gratisArtikel && verkoopprijs.compareTo(getMinimaleVerkoopprijs()) < 0) {
            errors.add("Verkoopprijs moet groter zijn dan minimale verkoopprijs");
        }

        return errors;
    }

    public BigDecimal getVerkoopprijs() {
        return verkoopprijs;
    }


    public BigDecimal getMinimaleVerkoopprijs() {
        if (aankoopprijs.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        if (winstmargeType == WinstmargeType.PERCENTAGE) {
            BigDecimal percentage = BigDecimal.ONE.add(
                    minimaleWinstmarge.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
            return aankoopprijs.multiply(percentage).setScale(2, RoundingMode.HALF_UP);
        }
        return aankoopprijs.add(minimaleWinstmarge).setScale(2, RoundingMode.HALF_UP);
    }

    // Getters
    public BigDecimal getAankoopprijs() {
        return aankoopprijs;
    }

    public BigDecimal getMinimaleWinstmarge() {
        return minimaleWinstmarge;
    }

    public WinstmargeType getWinstmargeType() {
        return winstmargeType;
    }

    // Setters
    public void setAankoopprijs(BigDecimal aankoopprijs) {
        if (aankoopprijs == null) {
            throw new IllegalArgumentException("Aankoopprijs mag niet null zijn");
        }
        validateParameters(aankoopprijs, this.minimaleWinstmarge);
        this.aankoopprijs = aankoopprijs.setScale(2, RoundingMode.HALF_UP);
    }

    public void setMinimaleWinstmarge(BigDecimal minimaleWinstmarge) {
        if (minimaleWinstmarge == null) {
            throw new IllegalArgumentException("Minimale winstmarge mag niet null zijn");
        }
        validateParameters(this.aankoopprijs, minimaleWinstmarge);
        this.minimaleWinstmarge = minimaleWinstmarge.setScale(2, RoundingMode.HALF_UP);
    }

    public void setWinstmargeType(WinstmargeType winstmargeType) {
        if (winstmargeType == null) {
            throw new IllegalArgumentException("Winstmarge type mag niet null zijn");
        }
        this.winstmargeType = winstmargeType;
    }

    @Override
    public String toString() {
        return "PrijsComponent{" +
               "aankoopprijs=" + aankoopprijs +
               ", minimaleWinstmarge=" + minimaleWinstmarge +
               ", winstmargeType=" + winstmargeType +
               ", minimaleVerkoopprijs=" + getMinimaleVerkoopprijs() +
               '}';
    }
}