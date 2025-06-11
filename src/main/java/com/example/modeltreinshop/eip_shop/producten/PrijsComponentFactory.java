package com.example.modeltreinshop.eip_shop.producten;

import java.math.BigDecimal;

public class PrijsComponentFactory {
    /* PrijsComponentFactory Class
     * Business Logic:
     * - Creates PrijsComponent instances
     * - Factory pattern implementation
     * - Ensures consistent price component creation
     * - Validates component parameters
     * - Maintains price component types
     */
    public static PrijsComponent createPrijsComponent(boolean isGratis,
                                                      BigDecimal aankoopprijs,
                                                      BigDecimal winstmarge,
                                                      WinstmargeType winstmargeType) {

        if (isGratis) {
            return PrijsComponent.createGratisArtikel();
        }

        return new PrijsComponent(aankoopprijs, winstmarge, winstmargeType);
    }
}
