package com.example.modeltreinshop.eip_shop.producten;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class PrijsComponent {

    @Column(name = "aankoopprijs")
    @NotNull(message = "Aankoopprijs mag niet null zijn")
    @PositiveOrZero(message = "Aankoopprijs moet groter zijn dan of gelijk aan 0")
    private BigDecimal aankoopprijs;

    @Column(name = "minimale_winstmarge")
    @NotNull(message = "Minimale winstmarge mag niet null zijn")
    @PositiveOrZero(message = "Minimale winstmarge moet groter zijn dan of gelijk aan 0")
    private BigDecimal minimaleWinstmarge;

    @Enumerated(EnumType.STRING)
    @Column(name = "winstmarge_type")
    @NotNull(message = "Winstmarge type mag niet null zijn")
    private WinstmargeType winstmargeType;

    @Column(name = "verkoopprijs")
    @NotNull(message = "Verkoopprijs mag niet null zijn")
    @PositiveOrZero(message = "Verkoopprijs moet groter zijn dan of gelijk aan 0")
    private BigDecimal verkoopprijs;

    @Column(name = "is_gratis_artikel")
    private boolean gratisArtikel = false;

    // Constructors and methods (omitted for brevity, assume they exist)

    public PrijsComponent(BigDecimal aankoopprijs,
                          BigDecimal minimaleWinstmarge,
                          WinstmargeType winstmargeType) {
        this.aankoopprijs = aankoopprijs.setScale(2, RoundingMode.HALF_UP);
        this.minimaleWinstmarge = minimaleWinstmarge.setScale(2, RoundingMode.HALF_UP);
        this.winstmargeType = winstmargeType;
    }
    // No-arg constructor for JPA
    public PrijsComponent() {
    }

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
        this.verkoopprijs = verkoopprijs.setScale(2, RoundingMode.HALF_UP);
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

    // Getters and setters (omitted for brevity, assume they exist)

    public BigDecimal getAankoopprijs() {
        return aankoopprijs;
    }

    public BigDecimal getMinimaleWinstmarge() {
        return minimaleWinstmarge;
    }

    public WinstmargeType getWinstmargeType() {
        return winstmargeType;
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