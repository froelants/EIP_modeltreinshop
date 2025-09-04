package com.example.modeltreinshop.eip_shop.klanten;

import com.example.modeltreinshop.eip_shop.winkelen.Winkelmandje;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Klant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long klantnummer;

    @Column
    @NotBlank(message = "Naam mag niet leeg zijn")
    private String naam;

    @Column
    @NotBlank(message = "E-mail mag niet leeg zijn")
    @Email(message = "E-mail formaat is ongeldig")
    private String email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "straat", column = @Column(name = "facturatie_straat")),
            @AttributeOverride(name = "huisnummer", column = @Column(name = "facturatie_huisnummer")),
            @AttributeOverride(name = "busnummer", column = @Column(name = "facturatie_busnummer")),
            @AttributeOverride(name = "postcode", column = @Column(name = "facturatie_postcode")),
            @AttributeOverride(name = "gemeente", column = @Column(name = "facturatie_gemeente"))
    })
    @NotNull(message = "Facturatieadres mag niet null zijn")
    private Adres facturatieadres;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "straat", column = @Column(name = "leverings_straat")),
            @AttributeOverride(name = "huisnummer", column = @Column(name = "leverings_huisnummer")),
            @AttributeOverride(name = "busnummer", column = @Column(name = "leverings_busnummer")),
            @AttributeOverride(name = "postcode", column = @Column(name = "leverings_postcode")),
            @AttributeOverride(name = "gemeente", column = @Column(name = "leverings_gemeente"))
    })
    private Adres leveringsadres;

    @ElementCollection
    @CollectionTable(name = "klant_telefoonnummers", joinColumns = @JoinColumn(name = "klant_id"))
    @Column(name = "telefoonnummer")
    @Size(max = 2, message = "Maximaal 2 telefoonnummers zijn toegestaan")
    private List<String> telefoonnummers = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "winkelmandje_id")
    private Winkelmandje winkelmandje;

    public Klant() {}

    public Klant(String naam, String email, Adres facturatieadres) {
        this.naam = naam;
        this.email = email;
        this.facturatieadres = facturatieadres;
        this.winkelmandje = new Winkelmandje();
    }

    // Getters en setters
    public Long getKlantnummer() {
        return klantnummer;
    }

    public void setKlantnummer(Long klantnummer) {
        this.klantnummer = klantnummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Adres getFacturatieadres() {
        return facturatieadres;
    }

    public void setFacturatieadres(Adres facturatieadres) {
        this.facturatieadres = facturatieadres;
    }

    public Adres getLeveringsadres() {
        return leveringsadres;
    }

    public void setLeveringsadres(Adres leveringsadres) {
        this.leveringsadres = leveringsadres;
    }

    public List<String> getTelefoonnummers() {
        return telefoonnummers;
    }

    public void setTelefoonnummers(List<String> telefoonnummers) {
        this.telefoonnummers = telefoonnummers;
    }

    public Winkelmandje getWinkelmandje() {
        return winkelmandje;
    }

    public void setWinkelmandje(Winkelmandje winkelmandje) {
        this.winkelmandje = winkelmandje;
    }
}