package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ArtikelInBackorder Tests")
class ArtikelInBackorderTest {

    private static final ArtikelTestData PREORDER_LOCOMOTIVE = new ArtikelTestData(
            "M37301",
            "Märklin 37301 DB BR 103.1",
            "Märklin",
            "E-Lok BR 103.1 DB EP IV Sound mfx+",
            new BigDecimal("429.99"),
            new BigDecimal("70.00"),
            WinstmargeType.PERCENTAGE,
            new BigDecimal("499.99"),
            List.of("37301-1.jpg", "37301-2.jpg"),
            false
    );

    private static final ArtikelTestData UPCOMING_WAGON = new ArtikelTestData(
            "R76543",
            "Roco 76543 DB Containerwagen",
            "Roco",
            "Containerwagen Sgns DB EP VI",
            new BigDecimal("39.99"),
            new BigDecimal("10.00"),
            WinstmargeType.EURO,
            new BigDecimal("49.99"),
            List.of("76543.jpg"),
            false
    );

    @ParameterizedTest
    @CsvFileSource(resources = "/artikelInBackorder/valid_backorder_data.csv", numLinesToSkip = 1)
    @DisplayName("Should create ArtikelInBackorder with valid backorder")
    void shouldCreateArtikelInBackorderWithValidBackorder(LocalDate expectedDate, int expectedAmount) {
        ArtikelInBackorder artikel = createArtikelWithBackorder(PREORDER_LOCOMOTIVE);

        artikel.addBackorder(expectedDate, expectedAmount);

        BackorderLijn backorder = artikel.getBackorders().get(0);
        assertThat(backorder.getExpectedDeliveryDate()).isEqualTo(expectedDate);
        assertThat(backorder.getExpectedAmount()).isEqualTo(expectedAmount);
    }

    @Test
    @DisplayName("Should handle multiple backorders correctly")
    void shouldHandleMultipleBackordersCorrectly() {
        ArtikelInBackorder artikel = createArtikelWithBackorder(PREORDER_LOCOMOTIVE);
        LocalDate now = LocalDate.now();

        artikel.addBackorder(now.plusWeeks(1), 5);
        artikel.addBackorder(now.plusWeeks(2), 10);
        artikel.addBackorder(now.plusWeeks(3), 15);

        assertThat(artikel.getBackorders())
                .hasSize(3)
                .isSortedBy(comparing(BackorderLijn::getExpectedDeliveryDate));
        assertThat(artikel.getTotalBackorderAmount()).isEqualTo(30);
    }

    @ParameterizedTest
    @MethodSource("invalidBackorderParameters")
    @DisplayName("Should throw exception for invalid backorder parameters")
    void shouldThrowExceptionForInvalidBackorderParameters(
            LocalDate expectedDate,
            int expectedAmount,
            String expectedMessage) {

        ArtikelInBackorder artikel = createArtikelWithBackorder(PREORDER_LOCOMOTIVE);

        assertThatThrownBy(() -> artikel.addBackorder(expectedDate, expectedAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    @DisplayName("Should remove backorder correctly")
    void shouldRemoveBackorderCorrectly() {
        ArtikelInBackorder artikel = createArtikelWithBackorder(PREORDER_LOCOMOTIVE);
        LocalDate expectedDate = LocalDate.now().plusWeeks(1);
        artikel.addBackorder(expectedDate, 5);

        artikel.removeBackorder(expectedDate);

        assertThat(artikel.getBackorders()).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when removing non-existent backorder")
    void shouldThrowExceptionWhenRemovingNonExistentBackorder() {
        ArtikelInBackorder artikel = createArtikelWithBackorder(PREORDER_LOCOMOTIVE);
        LocalDate nonExistentDate = LocalDate.now().plusWeeks(1);

        assertThatThrownBy(() -> artikel.removeBackorder(nonExistentDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Geen backorder gevonden voor deze datum");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/artikelInBackorder/backorder_updates.csv", numLinesToSkip = 1)
    @DisplayName("Should update backorder amount correctly")
    void shouldUpdateBackorderAmountCorrectly(
            LocalDate expectedDate,
            int initialAmount,
            int updateAmount,
            int expectedFinalAmount) {

        ArtikelInBackorder artikel = createArtikelWithBackorder(PREORDER_LOCOMOTIVE);
        artikel.addBackorder(expectedDate, initialAmount);

        artikel.updateBackorderAmount(expectedDate, updateAmount);

        assertThat(artikel.getBackorders().get(0).getExpectedAmount())
                .isEqualTo(expectedFinalAmount);
    }

    @Test
    @DisplayName("Should handle duplicate backorder dates")
    void shouldHandleDuplicateBackorderDates() {
        ArtikelInBackorder artikel = createArtikelWithBackorder(UPCOMING_WAGON);
        LocalDate date = LocalDate.now().plusWeeks(1);

        artikel.addBackorder(date, 5);

        assertThatThrownBy(() -> artikel.addBackorder(date, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Er bestaat al een backorder voor deze datum");
    }

    private static Stream<Arguments> invalidBackorderParameters() {
        return Stream.of(
                Arguments.of(null, 5, "Verwachte leverdatum mag niet null zijn"),
                Arguments.of(LocalDate.now().minusDays(1), 5, "Verwachte leverdatum moet in de toekomst liggen"),
                Arguments.of(LocalDate.now().plusDays(1), 0, "Verwacht aantal moet positief zijn"),
                Arguments.of(LocalDate.now().plusDays(1), -1, "Verwacht aantal moet positief zijn")
                        );
    }

    private ArtikelInBackorder createArtikelWithBackorder(ArtikelTestData data) {
        return new ArtikelInBackorder(
                data.artikelnummer(),
                data.naam(),
                data.merk(),
                data.omschrijving(),
                data.gratisArtikel(),
                data.aankoopprijs(),
                data.winstmarge(),
                data.winstmargeType(),
                data.verkoopprijs(),
                data.afbeeldingen()
        );
    }

    private record ArtikelTestData(
            String artikelnummer,
            String naam,
            String merk,
            String omschrijving,
            BigDecimal aankoopprijs,
            BigDecimal winstmarge,
            WinstmargeType winstmargeType,
            BigDecimal verkoopprijs,
            List<String> afbeeldingen,
            boolean gratisArtikel
    ) {}
}