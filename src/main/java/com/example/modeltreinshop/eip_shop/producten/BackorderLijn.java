package com.example.modeltreinshop.eip_shop.producten;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
public class BackorderLijn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Verwachte leverdatum mag niet null zijn")
    private LocalDate verwachteDatum;

    @Positive(message = "Aantal moet positief zijn")
    private int aantal;

    public BackorderLijn(LocalDate verwachteDatum, int aantal) {
        this.verwachteDatum = verwachteDatum;
        this.aantal = aantal;
    }
    // No-arg constructor for JPA
    public BackorderLijn() {
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

    // Getters and setters (omitted for brevity, assume they exist)
    public void setExpectedAmount(int expectedAmount) {
        this.aantal = expectedAmount;
    }
    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.verwachteDatum = expectedDeliveryDate;
    }
}