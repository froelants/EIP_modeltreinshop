package com.example.modeltreinshop.eip_shop.producten;

import com.example.modeltreinshop.eip_shop.producten.BackorderLijn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("artikel/create_and_populate_db.sql")
@DisplayName("BackorderLijnRepository Tests")
class BackorderLijnRepositoryTest {

    @Autowired
    private BackorderLijnRepository backorderLijnRepository;

    @Test
    @DisplayName("Should find BackorderLijn by ID")
    void shouldFindBackorderLijnById() {
        // Arrange is done by the @Sql annotation, find an ID from the script
        // assuming first backorder_lijn has id 1
        Long backorderLijnId = 1L;

        // Act
        Optional<BackorderLijn> backorderLijnOptional = backorderLijnRepository.findById(backorderLijnId);

        // Assert
        assertThat(backorderLijnOptional).isPresent();
        assertThat(backorderLijnOptional.get().getAantal()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should save a new BackorderLijn")
    void shouldSaveNewBackorderLijn() {
        // Arrange
        BackorderLijn newBackorderLijn = new BackorderLijn(LocalDate.now().plusDays(30), 25);

        // Act
        BackorderLijn savedBackorderLijn = backorderLijnRepository.save(newBackorderLijn);

        // Assert
        assertThat(savedBackorderLijn).isNotNull();
        assertThat(savedBackorderLijn.getAantal()).isEqualTo(25);
    }
}