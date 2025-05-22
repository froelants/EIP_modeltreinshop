package com.example.modeltreinshop.eip_shop.producten.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artikelen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artikel implements Serializable { // Implementeert Serializable vanwege de oorspronkelijke ArtikelMetNummer
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nummer", unique = true, nullable = false, length = 50)
    private String nummer;

    @Column(name = "naam", nullable = false)
    private String naam;

    @Lob // Voor grote tekstvelden
    @Column(name = "omschrijving")
    private String omschrijving;

    @Column(name = "merk", length = 100)
    private String merk;

    @Column(name = "eenmalig_artikel", nullable = false)
    private boolean eenmaligArtikel;

    @OneToMany(mappedBy = "artikel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArtikelAfbeelding> afbeeldingen = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addAfbeelding(ArtikelAfbeelding afbeelding) {
        this.afbeeldingen.add(afbeelding);
        afbeelding.setArtikel(this);
    }

    public void removeAfbeelding(ArtikelAfbeelding afbeelding) {
        this.afbeeldingen.remove(afbeelding);
        afbeelding.setArtikel(null);
    }
}
