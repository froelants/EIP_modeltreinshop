package com.example.modeltreinshop.eip_shop.winkelen;

import com.example.modeltreinshop.eip_shop.klanten.Klant;
import com.example.modeltreinshop.eip_shop.producten.Artikel;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Winkelmandje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "winkelmandje")
    private Klant klant;

    @ManyToMany
    @JoinTable(name = "winkelmandje_artikelen",
            joinColumns = @JoinColumn(name = "winkelmandje_id"),
            inverseJoinColumns = @JoinColumn(name = "artikelnummer"))
    private Set<Artikel> artikelen = new HashSet<>();

    public Winkelmandje() {}

    // Getters en setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Klant getKlant() {
        return klant;
    }

    public void setKlant(Klant klant) {
        this.klant = klant;
    }

    public Set<Artikel> getArtikelen() {
        return artikelen;
    }

    public void setArtikelen(Set<Artikel> artikelen) {
        this.artikelen = artikelen;
    }
}