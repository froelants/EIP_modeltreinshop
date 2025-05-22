package com.example.modeltreinshop.eip_shop.producten.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth; // Dit type kan niet direct gemapt worden

@Entity
@Table(name = "artikel_in_voorbestelling")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtikelInVoorbestelling {
    @Id
    @Column(name = "artikel_id")
    private Long artikelId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "artikel_id", referencedColumnName = "id")
    private Artikel artikel;

    @Column(name = "aankoopprijs", nullable = false, precision = 19, scale = 4)
    private BigDecimal aankoopprijs;

    @Column(name = "minimale_winstmarge", nullable = false, precision = 19, scale = 4)
    private BigDecimal minimaleWinstmarge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winstmarge_type_id", nullable = false)
    private WinstmargeType winstmargeType;

    @Column(name = "in_voorbestelling", nullable = false)
    private boolean inVoorbestelling;

    @Column(name = "prijs_in_voorbestelling", precision = 19, scale = 4)
    private BigDecimal prijsInVoorbestelling;

    @Column(name = "in_voorbestelling_tot_datum")
    private LocalDate inVoorbestellingTotDatum;

    // Custom mapping voor YearMonth
    @Column(name = "leveringskwartaal_jaar")
    private Integer leveringskwartaalJaar;

    @Column(name = "leveringskwartaal_maand")
    private Integer leveringskwartaalMaand;

    @Column(name = "onbekende_leveringstijd", nullable = false)
    private boolean onbekendeLeveringstijd;

    // Je kunt een transient getter/setter toevoegen voor YearMonth als gemak,
    // maar de mapping gebeurt via de aparte kolommen.
    @Transient // Wordt niet gepersisteerd
    public YearMonth getLeveringsKwartaal() {
        if (leveringskwartaalJaar != null && leveringskwartaalMaand != null) {
            return YearMonth.of(leveringskwartaalJaar, leveringskwartaalMaand);
        }
        return null;
    }

    public void setLeveringsKwartaal(YearMonth leveringsKwartaal) {
        if (leveringsKwartaal != null) {
            this.leveringskwartaalJaar = leveringsKwartaal.getYear();
            this.leveringskwartaalMaand = leveringsKwartaal.getMonthValue();
        } else {
            this.leveringskwartaalJaar = null;
            this.leveringskwartaalMaand = null;
        }
    }
}