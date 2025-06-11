package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ArtikelInBackorder Tests")
class ArtikelInBackorderTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/ArtikelInBackorder_ValidArticles.csv", numLinesToSkip = 1)
    @DisplayName("Should create ArtikelInBackorder with valid data")
    void shouldCreateArtikelInBackorder(
            String artikelnummer,
            String naam,
            String merk,
            String omschrijving,
            boolean gratisArtikel,
            BigDecimal aankoopprijs,
            BigDecimal winstmarge,
            WinstmargeType winstmargeType,
            BigDecimal verkoopprijs,
            String afbeeldingen,
            int voorraad,
            int minimaleVoorraad,
            int normaleVoorraad,
            int minimaleBestelhoeveelheid,
            LocalDate verwachteDatum,
            int aantalInBackorder) {

        List<String> afbeeldingenList = List.of(afbeeldingen.split(";"));
        LocalDate besteldatum = LocalDate.now();

        ArtikelInBackorder artikel = new ArtikelInBackorder(
                artikelnummer, naam, merk, omschrijving, gratisArtikel,
                aankoopprijs, winstmarge, winstmargeType, verkoopprijs,
                afbeeldingenList, voorraad, minimaleVoorraad, normaleVoorraad,
                minimaleBestelhoeveelheid, besteldatum);

        int backorderId = artikel.addBackorderLijn(verwachteDatum, aantalInBackorder);

        assertThat(artikel.getBesteldatum()).isEqualTo(besteldatum);
        assertThat(artikel.getBackorders()).hasSize(1);
        assertThat(artikel.getAantalInBackorder()).isEqualTo(aantalInBackorder);
        assertThat(artikel.getBackorders().get(backorderId).getVerwachteDatum())
                .isEqualTo(verwachteDatum);
    }

    @Test
    @DisplayName("Should throw exception when besteldatum is null")
    void shouldThrowExceptionWhenBesteldatumIsNull() {
        assertThatThrownBy(() -> new ArtikelInBackorder(
                "36435", "DB BR 243", "Märklin", "Test lok", false,
                new BigDecimal("329.99"), new BigDecimal("45.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("478.49"),
                List.of("36435.jpg"), 0, 5, 15, 2, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Besteldatum mag niet null zijn");
    }

    @ParameterizedTest
    @MethodSource("multipleBackorderLines")
    @DisplayName("Should handle multiple backorder lines correctly")
    void shouldHandleMultipleBackorderLines(
            List<BackorderTestData> backorderLines,
            int expectedTotalAmount) {

        ArtikelInBackorder artikel = createTestArtikel();

        backorderLines.forEach(line ->
                                       artikel.addBackorderLijn(line.verwachteDatum(), line.aantal()));

        assertThat(artikel.getAantalInBackorder()).isEqualTo(expectedTotalAmount);
        assertThat(artikel.getBackorders()).hasSize(backorderLines.size());
    }

    @Test
    @DisplayName("Should remove backorder lines correctly")
    void shouldRemoveBackorderLines() {
        ArtikelInBackorder artikel = createTestArtikel();
        int id1 = artikel.addBackorderLijn(LocalDate.now().plusDays(7), 5);
        int id2 = artikel.addBackorderLijn(LocalDate.now().plusDays(14), 10);

        artikel.removeBackorderLijn(id1);
        assertThat(artikel.getAantalInBackorder()).isEqualTo(10);

        artikel.removeBackorderLijn(id2);
        assertThat(artikel.getAantalInBackorder()).isZero();
        assertThat(artikel.getBackorders()).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when removing non-existent backorder")
    void shouldThrowExceptionWhenRemovingNonExistentBackorder() {
        ArtikelInBackorder artikel = createTestArtikel();

        assertThatThrownBy(() -> artikel.removeBackorderLijn(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Backorder ID bestaat niet");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    @DisplayName("Should throw exception for invalid backorder amounts")
    void shouldThrowExceptionForInvalidBackorderAmounts(int invalidAmount) {
        ArtikelInBackorder artikel = createTestArtikel();

        assertThatThrownBy(() ->
                                   artikel.addBackorderLijn(LocalDate.now().plusDays(7), invalidAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Aantal moet positief zijn");
    }

    @Test
    @DisplayName("Should handle toString correctly")
    void shouldHandleToStringCorrectly() {
        ArtikelInBackorder artikel = createTestArtikel();
        LocalDate verwachteDatum = LocalDate.now().plusDays(7);
        artikel.addBackorderLijn(verwachteDatum, 5);

        String result = artikel.toString();
        assertThat(result)
                .contains("Backordergegevens:")
                .contains("Besteldatum:")
                .contains("artikelen verwacht op");
    }

    private static Stream<Arguments> multipleBackorderLines() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new BackorderTestData(LocalDate.now().plusDays(7), 5),
                                new BackorderTestData(LocalDate.now().plusDays(14), 10)
                               ),
                        15
                            ),
                Arguments.of(
                        List.of(
                                new BackorderTestData(LocalDate.now().plusDays(7), 3),
                                new BackorderTestData(LocalDate.now().plusDays(14), 7),
                                new BackorderTestData(LocalDate.now().plusDays(21), 5)
                               ),
                        15
                            )
                        );
    }

    private ArtikelInBackorder createTestArtikel() {
        return new ArtikelInBackorder(
                "36435", "DB BR 243", "Märklin", "Test lok", false,
                new BigDecimal("329.99"), new BigDecimal("45.00"),
                WinstmargeType.PERCENTAGE, new BigDecimal("478.49"),
                List.of("36435.jpg"), 0, 5, 15, 2,
                LocalDate.now()
        );
    }

    private record BackorderTestData(LocalDate verwachteDatum, int aantal) {}
}