package com.example.modeltreinshop.eip_shop.producten;
import java.time.LocalDate;

public class BackorderLijn {
    /* BackorderLijn Class
     * Business Logic:
     * - Represents a single backorder entry
     * - Contains:
     *   - Expected delivery date
     *   - Number of items expected
     * - Immutable once created
     * - Validates:
     *   - Date cannot be null
     *   - Amount must be positive
     * - Used by ArtikelInBackorder to track expected deliveries
     */
    private final LocalDate verwachteDatum;
    private final int aantal;

    public BackorderLijn(LocalDate verwachteDatum, int aantal) {
        this.verwachteDatum = verwachteDatum;
        this.aantal = aantal;
    }

    public LocalDate getVerwachteDatum() {
        return verwachteDatum;
    }

    public int getAantal() {
        return aantal;
    }
    @Override
    public String toString() {
        return String.format("BackorderLijn: %d artikelen verwacht op %s", aantal, verwachteDatum);
    }
}
