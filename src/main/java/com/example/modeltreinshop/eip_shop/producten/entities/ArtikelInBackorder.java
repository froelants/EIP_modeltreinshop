package com.example.modeltreinshop.eip_shop.producten.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "artikel_in_backorder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtikelInBackorder {
    @Id
    @Column(name = "artikel_id")
    private Long artikelId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "artikel_id", referencedColumnName = "id")
    private Artikel artikel;

    @Column(name = "besteldatum", nullable = false)
    private LocalDate besteldatum;

    @Column(name = "aantal_in_backorder", nullable = false)
    private int aantalInBackorder;

    @Column(name = "verwachte_leveringstijd_in_dagen", nullable = false)
    private int verwachteLeveringstijdInDagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artikel_in_voorraad_id", nullable = false)
    private ArtikelInVoorraad artikelInVoorraad;
}