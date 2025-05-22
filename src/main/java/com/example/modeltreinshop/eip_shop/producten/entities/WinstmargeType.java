package com.example.modeltreinshop.eip_shop.producten.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "winstmarge_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WinstmargeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type_naam", nullable = false, unique = true)
    private String typeNaam;

    // Opmerking: In een echte applicatie zou je hier mogelijk de WinstmargeStrategy logica koppelen,
    // bijv. via een transient veld of een service die de strategie op basis van typeNaam levert.
    // Echter, de strategie zelf is geen persistente entiteit.
}
