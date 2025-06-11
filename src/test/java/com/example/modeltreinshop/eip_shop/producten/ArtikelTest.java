package com.example.modeltreinshop.eip_shop.producten;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Artikel Tests")
class ArtikelTest {

    // First, create a concrete test implementation
    private static class TestArtikel
            extends Artikel {
        public TestArtikel(String artikelnummer,
                           String naam,
                           String merk,
                           String omschrijving,
                           boolean gratisArtikel,
                           BigDecimal aankoopprijs,
                           BigDecimal winstmarge,
                           WinstmargeType type,
                           BigDecimal verkoopprijs,
                           List<String> afbeeldingen) {
            super(artikelnummer,
                  naam,
                  merk,
                  omschrijving,
                  gratisArtikel,
                  aankoopprijs,
                  winstmarge,
                  type,
                  verkoopprijs,
                  afbeeldingen);
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/Artikel_ValidArticles.csv", numLinesToSkip = 1)
        @DisplayName("Should create valid article")
        void shouldCreateValidArticle(
                String artikelnummer,
                String naam,
                String merk,
                String omschrijving,
                boolean gratisArtikel,
                BigDecimal aankoopprijs,
                BigDecimal winstmarge,
                WinstmargeType type,
                BigDecimal verkoopprijs,
                List<String> afbeeldingen) {

            Artikel artikel = new TestArtikel(artikelnummer,
                                              naam,
                                              merk,
                                              omschrijving,
                                              gratisArtikel,
                                              aankoopprijs,
                                              winstmarge,
                                              type,
                                              verkoopprijs,
                                              afbeeldingen);

            assertThat(artikel.getArtikelnummer()).isEqualTo(artikelnummer);
            assertThat(artikel.getNaam()).isEqualTo(naam);
            assertThat(artikel.getOmschrijving()).isEqualTo(omschrijving);
            assertThat(artikel
                               .getPrijsComponent()
                               .getAankoopprijs())
                    .isEqualByComparingTo(aankoopprijs);
            assertThat(artikel
                               .getPrijsComponent()
                               .getMinimaleWinstmarge())
                    .isEqualByComparingTo(winstmarge);
            assertThat(artikel
                               .getPrijsComponent()
                               .getWinstmargeType())
                    .isEqualTo(type);
            assertThat(artikel
                               .getPrijsComponent()
                               .getVerkoopprijs())
                    .isEqualByComparingTo(verkoopprijs);
            assertThat(artikel.getAfbeeldingen()).isEqualTo(afbeeldingen);
        }

        @ParameterizedTest
        @ValueSource(strings = {" ", "\t", "\n", "\r", "\u0000", "\u200B"})
        @DisplayName("Should throw exception for whitespace-only strings")
        void shouldThrowExceptionForWhitespaceOnlyStrings(String input) {
            assertThatThrownBy(() -> new TestArtikel(input,
                                                     "testNaam",
                                                     "testMerk",
                                                     "TestOmschrijving",
                                                     false,
                                                     BigDecimal.TEN,
                                                     BigDecimal.ONE,
                                                     WinstmargeType.EURO,
                                                     BigDecimal.valueOf(11),
                                                     List.of("testImage.jpg")))
                                                                            .isInstanceOf(IllegalArgumentException.class)
                                                                            .hasMessageContaining("Verplicht veld mag niet leeg zijn");
        }

        @Nested
        @DisplayName("Price Component Tests")
        class PriceComponentTests {

            @ParameterizedTest
            @CsvFileSource(resources = "/Artikel_PriceCalculations.csv", numLinesToSkip = 1)
            @DisplayName("Should calculate correct sales prices")
            void shouldCalculateCorrectSalesPrices(
                    String artikelnummer,
                    String naam,
                    BigDecimal aankoopprijs,
                    BigDecimal winstmarge,
                    WinstmargeType type,
                    BigDecimal expectedVerkoopprijs,
                    BigDecimal verkoopprijs,
                    List<String> afbeeldingen) {

                Artikel artikel = new TestArtikel(artikelnummer,
                                                  naam,
                                                  "TestMerk",
                                                  "Test omschrijving",
                                                  false,
                                                  aankoopprijs,
                                                  winstmarge,
                                                  type,
                                                  verkoopprijs,
                                                  afbeeldingen);
                assertThat(artikel
                                   .getPrijsComponent()
                                   .getVerkoopprijs())
                        .isEqualByComparingTo(expectedVerkoopprijs);
            }
        }

        @Nested
        @DisplayName("Real-world Product Tests")
        class RealWorldProductTests {

            @ParameterizedTest
            @CsvFileSource(resources = "/real_products.csv", numLinesToSkip = 1)
            @DisplayName("Should create valid articles from real product data")
            void shouldCalculateCorrectSalesPrices(
                    String artikelnummer,
                    String naam,
                    String merk,
                    String omschrijving,
                    boolean gratisArtikel,
                    BigDecimal aankoopprijs,
                    BigDecimal winstmarge,
                    WinstmargeType type,
                    BigDecimal verkoopprijs,
                    List<String> afbeeldingen,
                    BigDecimal expectedVerkoopprijs) {

                Artikel artikel = new TestArtikel(artikelnummer,      // artikelnummer
                                                  naam,                // naam
                                                  merk,          // merk
                                                  omschrijving, // omschrijving
                                                  gratisArtikel,               // gratisArtikel
                                                  aankoopprijs,        // aankoopprijs
                                                  winstmarge,          // winstmarge
                                                  type,                // type
                                                  verkoopprijs,        // verkoopprijs
                                                  afbeeldingen);       // afbeeldingen

                assertThat(artikel.getPrijsComponent().getVerkoopprijs())
                        .isEqualByComparingTo(expectedVerkoopprijs);
            }

            @Test
            @DisplayName("Should handle long product descriptions")
            void shouldHandleLongProductDescriptions() {
                String longDescription = "H0 DB BR 193 320-1 'Vectron' Electric Locomotive Era VI...";

                Artikel artikel1 = new TestArtikel("39829",           // artikelnummer
                                                   "Märklin BR 182",    // naam
                                                   "Märklin",           // merk (added)
                                                   longDescription,     // omschrijving
                                                   false,               // gratisArtikel
                                                   new BigDecimal("429.99"),
                                                   new BigDecimal("70.00"),
                                                   WinstmargeType.EURO,
                                                   new BigDecimal("499.99"),
                                                   List.of("39829-1.jpg", "39829-2.jpg"));

                assertThat(artikel1.getOmschrijving()).isEqualTo(longDescription);
            }

            @Nested
            @DisplayName("Equals and HashArtikelnummer Tests")
            class EqualsAndHashArtikelnummerTests {

                @Test
                @DisplayName("Should be equal based on artikelnummer")
                void shouldBeEqualBasedOnArtikelnummer() {
                    Artikel artikel1 = new TestArtikel("39829",
                                                       "Märklin BR 182",
                                                       "TestMerk",
                                                       "omschrijving",
                                                       false,
                                                       new BigDecimal("429.99"),
                                                       new BigDecimal("70.00"),
                                                       WinstmargeType.EURO,
                                                       new BigDecimal("499.99"),
                                                       List.of("39829-1.jpg"));

                    Artikel artikel2 = new TestArtikel("39829",
                                                       "Different Name",
                                                       "TestMerk",
                                                       "other description",
                                                       false,
                                                       new BigDecimal("400.00"),
                                                       new BigDecimal("80.00"),
                                                       WinstmargeType.PERCENTAGE,
                                                       new BigDecimal("720.00"),
                                                       List.of("39829-2.jpg"));
                    assertThat(artikel1).isEqualTo(artikel2);
                    assertThat(artikel1.hashCode()).isEqualTo(artikel2.hashCode());
                }

                private static Stream<Arguments> invalidArticleParameters() {
                    return Stream.of(
                            // Null checks
                            Arguments.of(null,
                                         // artikelnummer
                                         "naam",
                                         "TestMerk",
                                         "omschrijving",
                                         false,
                                         BigDecimal.TEN,
                                         BigDecimal.ONE,
                                         WinstmargeType.EURO,
                                         BigDecimal.valueOf(11),
                                         List.of("test.jpg"),
                                         "Verplicht veld mag niet null zijn"),

                            Arguments.of("artikelnummer",
                                         null,
                                         // naam
                                         "TestMerk",
                                         "omschrijving",
                                         false,
                                         BigDecimal.TEN,
                                         BigDecimal.ONE,
                                         WinstmargeType.EURO,
                                         BigDecimal.valueOf(11),
                                         List.of("test.jpg"),
                                         "Verplicht veld mag niet null zijn"),

                            // Blank checks
                            Arguments.of("",
                                         // artikelnummer
                                         "naam",
                                         "TestMerk",
                                         "omschrijving",
                                         false,
                                         BigDecimal.TEN,
                                         BigDecimal.ONE,
                                         WinstmargeType.EURO,
                                         BigDecimal.valueOf(11),
                                         List.of("test.jpg"),
                                         "Verplicht veld mag niet leeg zijn"),

                            // Invalid prices
                            Arguments.of("artikelnummer",
                                         "naam",
                                         "TestMerk",
                                         "omschrijving",
                                         false,
                                         BigDecimal.ZERO,
                                         // aankoopprijs
                                         BigDecimal.ONE,
                                         WinstmargeType.EURO,
                                         BigDecimal.valueOf(11),
                                         List.of("test.jpg"),
                                         "Prijs moet positief zijn"),

                            Arguments.of("artikelnummer",
                                         "naam",
                                         "TestMerk",
                                         "omschrijving",
                                         false,
                                         BigDecimal.TEN,
                                         BigDecimal.ZERO,
                                         // winstmarge
                                         WinstmargeType.EURO,
                                         BigDecimal.valueOf(11),
                                         List.of("test.jpg"),
                                         "Winstmarge moet positief zijn")
                                    );
                }
            }
        }
    }
}