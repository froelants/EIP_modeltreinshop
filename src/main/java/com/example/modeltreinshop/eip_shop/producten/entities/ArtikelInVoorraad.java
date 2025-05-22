package com.example.modeltreinshop.eip_shop.producten.entities;

package com.jouwbedrijf.artikelbeheer.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "artikel_in_voorraad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtikelInVoorraad {
    @Id
    @Column(name = "artikel_id")
    private Long artikelId; // Dit is de FK naar de artikelen tabel

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Gebruikt de PK van de geassocieerde entiteit als zijn eigen PK
    @JoinColumn(name = "artikel_id", referencedColumnName = "id")
    private Artikel artikel;

    @Column(name = "aankoopprijs", nullable = false, precision = 19, scale = 4)
    private BigDecimal aankoopprijs;

    @Column(name = "minimale_winstmarge", nullable = false, precision = 19, scale = 4)
    private BigDecimal minimaleWinstmarge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winstmarge_type_id", nullable = false)
    private WinstmargeType winstmargeType;

    @Column(name = "laatste_aankoopdatum")
    private LocalDate laatsteAankoopdatum;
}