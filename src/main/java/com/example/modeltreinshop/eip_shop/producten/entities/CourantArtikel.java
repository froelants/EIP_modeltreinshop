package com.example.modeltreinshop.eip_shop.producten.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "courant_artikelen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourantArtikel {
    @Id
    @Column(name = "artikel_id")
    private Long artikelId; // Dit is de FK naar de artikel_in_voorraad tabel

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Gebruikt de PK van de geassocieerde entiteit als zijn eigen PK
    @JoinColumn(name = "artikel_id", referencedColumnName = "artikel_id") // Verwijst naar artikel_in_voorraad.artikel_id
    private ArtikelInVoorraad artikelInVoorraad;

    @Column(name = "in_korting", nullable = false)
    private boolean inKorting;

    @Column(name = "korting_tot_datum")
    private LocalDate kortingTotDatum;

    @Column(name = "korting_tot_rest")
    private Integer kortingTotRest; // Gebruik Integer voor nullable int

    @Column(name = "prijs_in_korting", precision = 19, scale = 4)
    private BigDecimal prijsInKorting;

    @Column(name = "maximale_korting", precision = 19, scale = 4)
    private BigDecimal maximaleKorting;

    @Column(name = "minimale_voorraad")
    private Integer minimaleVoorraad;

    @Column(name = "normale_voorraad")
    private Integer normaleVoorraad;

    @Column(name = "minimale_bestelhoeveelheid")
    private Integer minimaleBestelhoeveelheid;
}