package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;

public class PrijsComponent {
    private BigDecimal aankoopprijs;
    private BigDecimal minimaleWinstmarge;
    private WinstmargeType winstmargeType;

    public PrijsComponent(BigDecimal aankoopprijs, BigDecimal minimaleWinstmarge,
                          WinstmargeType winstmargeType) {
        valideerPrijsParameters(aankoopprijs, minimaleWinstmarge, winstmargeType);
        this.aankoopprijs = aankoopprijs.setScale(2);
        this.minimaleWinstmarge = minimaleWinstmarge.setScale(2);
        this.winstmargeType = winstmargeType;
    }

    private void valideerPrijsParameters(BigDecimal aankoopprijs,
                                         BigDecimal minimaleWinstmarge, WinstmargeType winstmargeType) {
        if (aankoopprijs == null || minimaleWinstmarge == null || winstmargeType == null) {
            throw new IllegalArgumentException("Prijs parameters mogen niet null zijn");
        }
        if (aankoopprijs.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Aankoopprijs moet positief zijn");
        }
        if (minimaleWinstmarge.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimale winstmarge mag niet negatief zijn");
        }
    }

    public void setAankoopprijs(BigDecimal aankoopprijs) {
        this.aankoopprijs = aankoopprijs;
        valideerPrijsParameters(aankoopprijs, minimaleWinstmarge, winstmargeType);
    }

    public void setMinimaleWinstmarge(BigDecimal minimaleWinstmarge) {
        this.minimaleWinstmarge = minimaleWinstmarge;
        valideerPrijsParameters(aankoopprijs, minimaleWinstmarge, winstmargeType);
    }

    public void setWinstmargeType(WinstmargeType winstmargeType) {
        this.winstmargeType = winstmargeType;
        valideerPrijsParameters(aankoopprijs, minimaleWinstmarge, winstmargeType);
    }

    public BigDecimal berekenVerkoopprijs() {
        return winstmargeType.getStrategy()
                .berekenVerkoopprijs(aankoopprijs, minimaleWinstmarge);
    }

    public BigDecimal berekenWinst() {
        return berekenVerkoopprijs().subtract(aankoopprijs).setScale(2);
    }
    public BigDecimal berekenWinstMarge() {
        return berekenWinst().multiply(minimaleWinstmarge).setScale(2);
    }
    public BigDecimal berekenWinstMargePercentage() {
        return berekenWinstMarge().divide(aankoopprijs, 2, BigDecimal.ROUND_HALF_UP);
    }
    public BigDecimal getMinimaleWinstmarge() {
        return minimaleWinstmarge;
    }

    public WinstmargeType getWinstmargeType() {
        return winstmargeType;
    }

    public BigDecimal getAankoopprijs() {
        return aankoopprijs;
    }
}
