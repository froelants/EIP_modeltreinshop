package com.example.modeltreinshop.eip_shop.producten.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArtikelDecoratorTest {
    private Artikel origineel;
    private Artikel decorator;

    @BeforeEach
    void beforeEach() {
        origineel = new ArtikelMetNummer(1, "TEST001", "TestMerk",
                                         "TestNaam", "TestOmschrijving", false);
        decorator = new GewoonArtikel(origineel);
    }

    @ParameterizedTest
    @CsvSource({
            "NieuweNaam, naam",
            "NieuwMerk, merk",
            "NieuweOmschrijving, omschrijving"
    })
    void setters_WijzigenWaardenInOrigineel(String nieuweWaarde, String veld) {
        switch (veld) {
            case "naam" -> {
                decorator.setNaam(nieuweWaarde);
                assertThat(origineel.getNaam()).isEqualTo(nieuweWaarde);
            }
            case "merk" -> {
                decorator.setMerk(nieuweWaarde);
                assertThat(origineel.getMerk()).isEqualTo(nieuweWaarde);
            }
            case "omschrijving" -> {
                decorator.setOmschrijving(nieuweWaarde);
                assertThat(origineel.getOmschrijving()).isEqualTo(nieuweWaarde);
            }
        }
    }

    @ParameterizedTest
    @CsvSource({
            "null, naam",
            "'  ', naam",
            "null, merk",
            "'  ', merk",
            "null, omschrijving",
            "'  ', omschrijving"
    })
    void setters_MetOngeldigeWaarden_GooienExceptions(String ongeldigeWaarde, String veld) {
        String waarde = "null".equals(ongeldigeWaarde) ? null : ongeldigeWaarde;

        assertThrows(IllegalArgumentException.class, () -> {
            switch (veld) {
                case "naam" -> decorator.setNaam(waarde);
                case "merk" -> decorator.setMerk(waarde);
                case "omschrijving" -> decorator.setOmschrijving(waarde);
            }
        });
    }

    @ParameterizedTest
    @CsvSource({
            "test1.jpg, true, true",    // Nieuwe afbeelding toevoegen
            "test2.jpg, false, false",  // Niet-bestaande afbeelding verwijderen
            "'  ', true, false",        // Lege afbeelding toevoegen
            "null, true, false"         // Null afbeelding toevoegen
    })
    void afbeeldingen_BeheerMetVerschillendeWaarden(String afbeelding,
                                                    boolean isToevoegen, boolean verwachtSucces) {
        String testAfbeelding = "null".equals(afbeelding) ? null : afbeelding;

        if (isToevoegen) {
            if (!verwachtSucces) {
                assertThrows(IllegalArgumentException.class,
                             () -> decorator.addAfbeelding(testAfbeelding));
            } else {
                assertTrue(decorator.addAfbeelding(testAfbeelding));
                assertThat(origineel.getAfbeeldingen()).contains(testAfbeelding);
            }
        } else {
            if (!verwachtSucces) {
                assertThrows(IllegalArgumentException.class,
                             () -> decorator.removeAfbeelding(testAfbeelding));
            } else {
                assertTrue(decorator.removeAfbeelding(testAfbeelding));
                assertThat(origineel.getAfbeeldingen()).doesNotContain(testAfbeelding);
            }
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1, TEST001, TestMerk, TestNaam, TestOmschrijving, false"
    })
    void getters_GevenWaardenVanOrigineel(long id, String nummer, String merk,
                                          String naam, String omschrijving, boolean eenmalig) {
        assertThat(decorator.getId()).isEqualTo(id);
        assertThat(decorator.getNummer()).isEqualTo(nummer);
        assertThat(decorator.getMerk()).isEqualTo(merk);
        assertThat(decorator.getNaam()).isEqualTo(naam);
        assertThat(decorator.getOmschrijving()).isEqualTo(omschrijving);
        assertThat(decorator.isEenmaligProduct()).isEqualTo(eenmalig);
    }

    @Test
    void nullArtikelWerptException() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                                                                new GewoonArtikel(null));
    }

    @Test
    void getAfbeeldingen_IsOnmodifiable() {
        decorator.addAfbeelding("test.jpg");
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> decorator.getAfbeeldingen().add("nieuw.jpg"));
    }
}

