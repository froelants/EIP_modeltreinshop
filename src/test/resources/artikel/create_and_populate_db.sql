-- backorder_lijn_repository_test.sql

-- Drop tables and constraints to ensure a clean state
DROP TABLE IF EXISTS `klant_telefoonnummers`;
DROP TABLE IF EXISTS `winkelmandje_artikelen`;
DROP TABLE IF EXISTS `winkelmandje`;
DROP TABLE IF EXISTS `klant`;
DROP TABLE IF EXISTS `artikel_afbeeldingen`;
DROP TABLE IF EXISTS `backorder_lijn`;
DROP TABLE IF EXISTS `artikel`;
DROP TABLE IF EXISTS `land`;
DROP TABLE IF EXISTS `btw_percentage`;

-- Create tables
CREATE TABLE `btw_percentage` (
                                  `btw_id` BIGINT NOT NULL AUTO_INCREMENT,
                                  `percentage` DECIMAL(5, 2) NOT NULL,
                                  PRIMARY KEY (`btw_id`)
);

CREATE TABLE `land` (
                        `landcode` VARCHAR(2) NOT NULL,
                        `landnaam` VARCHAR(255) NOT NULL,
                        `btw_id` BIGINT NOT NULL,
                        PRIMARY KEY (`landcode`),
                        CONSTRAINT `FK_land_btw` FOREIGN KEY (`btw_id`) REFERENCES `btw_percentage`(`btw_id`)
);

CREATE TABLE `klant` (
                         `klantnummer` BIGINT NOT NULL AUTO_INCREMENT,
                         `naam` VARCHAR(255) NOT NULL,
                         `email` VARCHAR(255) NOT NULL,
                         `facturatie_straat` VARCHAR(255) NOT NULL,
                         `facturatie_huisnummer` VARCHAR(255) NOT NULL,
                         `facturatie_busnummer` VARCHAR(255),
                         `facturatie_postcode` VARCHAR(255) NOT NULL,
                         `facturatie_gemeente` VARCHAR(255) NOT NULL,
                         `facturatie_land_code` VARCHAR(2) NOT NULL,
                         `leverings_straat` VARCHAR(255) NOT NULL,
                         `leverings_huisnummer` VARCHAR(255) NOT NULL,
                         `leverings_busnummer` VARCHAR(255),
                         `leverings_postcode` VARCHAR(255) NOT NULL,
                         `leverings_gemeente` VARCHAR(255) NOT NULL,
                         `leverings_land_code` VARCHAR(2) NOT NULL,
                         `winkelmandje_id` BIGINT,
                         PRIMARY KEY (`klantnummer`),
                         CONSTRAINT `FK_klant_facturatie_land` FOREIGN KEY (`facturatie_land_code`) REFERENCES `land`(`landcode`),
                         CONSTRAINT `FK_klant_leverings_land` FOREIGN KEY (`leverings_land_code`) REFERENCES `land`(`landcode`)
);

CREATE TABLE `artikel` (
                           `artikelnummer` VARCHAR(255) NOT NULL,
                           `artikel_type` VARCHAR(31) NOT NULL,
                           `naam` VARCHAR(255) NOT NULL,
                           `merk` VARCHAR(255) NOT NULL,
                           `omschrijving` TEXT NOT NULL,
                           `kortings_prijs` DECIMAL(19, 2),
                           `korting_tot_datum` DATE,
                           `aankoopprijs` DECIMAL(19, 2) NOT NULL,
                           `is_gratis_artikel` BIT(1),
                           `minimale_winstmarge` DECIMAL(19, 2) NOT NULL,
                           `verkoopprijs` DECIMAL(19, 2) NOT NULL,
                           `winstmarge_type` VARCHAR(255) NOT NULL,
                           `voorraad` INT,
                           `laatste_aankoopdatum` DATE,
                           `minimale_voorraad` INT,
                           `normale_voorraad` INT,
                           `minimale_bestelhoeveelheid` INT,
                           `besteldatum` DATE,
                           `next_backorder_id` INT,
                           `voorbestellings_prijs` DECIMAL(19, 2),
                           `voorbestelling_tot_datum` DATE,
                           `leverings_kwartaal` VARCHAR(255),
                           `voorschot` DECIMAL(19, 2),
                           `zonder_leveringstijd` BIT(1),
                           `aantal_in_voorbestelling` INT,
                           `maximaal_aantal_voorbestellingen` INT,
                           PRIMARY KEY (`artikelnummer`)
);

CREATE TABLE `artikel_afbeeldingen` (
                                        `artikelnummer` VARCHAR(255) NOT NULL,
                                        `afbeelding_url` VARCHAR(255)
);

CREATE TABLE `backorder_lijn` (
                                  `id` BIGINT NOT NULL AUTO_INCREMENT,
                                  `aantal` INT NOT NULL,
                                  `verwachte_datum` DATE NOT NULL,
                                  `artikel_artikelnummer` VARCHAR(255) NOT NULL,
                                  `backorder_id` INT NOT NULL,
                                  PRIMARY KEY (`id`)
);

CREATE TABLE `winkelmandje` (
                                `id` BIGINT NOT NULL AUTO_INCREMENT,
                                PRIMARY KEY (`id`)
);

CREATE TABLE `klant_telefoonnummers` (
                                         `klant_id` BIGINT NOT NULL,
                                         `telefoonnummer` VARCHAR(255),
                                         CONSTRAINT `FK_klant_telefoonnummers_klant` FOREIGN KEY (`klant_id`) REFERENCES `klant`(`klantnummer`)
);

CREATE TABLE `winkelmandje_artikelen` (
                                          `winkelmandje_id` BIGINT NOT NULL,
                                          `artikelnummer` VARCHAR(255) NOT NULL,
                                          CONSTRAINT `FK_winkelmandje_artikelen_winkelmandje` FOREIGN KEY (`winkelmandje_id`) REFERENCES `winkelmandje`(`id`),
                                          CONSTRAINT `FK_winkelmandje_artikelen_artikel` FOREIGN KEY (`artikelnummer`) REFERENCES `artikel`(`artikelnummer`)
);

-- Foreign keys
ALTER TABLE `artikel_afbeeldingen` ADD CONSTRAINT `FK_artikel_afbeeldingen_artikel` FOREIGN KEY (`artikelnummer`) REFERENCES `artikel`(`artikelnummer`);
ALTER TABLE `backorder_lijn` ADD CONSTRAINT `FK_backorder_lijn_artikel` FOREIGN KEY (`artikel_artikelnummer`) REFERENCES `artikel`(`artikelnummer`);
ALTER TABLE `klant` ADD CONSTRAINT `FK_klant_winkelmandje` FOREIGN KEY (`winkelmandje_id`) REFERENCES `winkelmandje`(`id`);

-- Insert data for BackorderLijnRepositoryTest
INSERT INTO `btw_percentage` (`btw_id`, `percentage`) VALUES
                                                          (1, 21.00),
                                                          (2, 19.00);

INSERT INTO `land` (`landcode`, `landnaam`, `btw_id`) VALUES
                                                          ('BE', 'België', 1),
                                                          ('DE', 'Duitsland', 2);

INSERT INTO `klant` (`klantnummer`, `naam`, `email`,
                     `facturatie_straat`, `facturatie_huisnummer`, `facturatie_busnummer`, `facturatie_postcode`, `facturatie_gemeente`, `facturatie_land_code`,
                     `leverings_straat`, `leverings_huisnummer`, `leverings_busnummer`, `leverings_postcode`, `leverings_gemeente`, `leverings_land_code`,
                     `winkelmandje_id`) VALUES
    (1, 'Jan Peeters', 'jan.peeters@example.com',
     'Leuvensestraat', '15', 'bus A', '3000', 'Leuven', 'BE',
     'Kerkstraat', '22', NULL, '3000', 'Leuven', 'BE', NULL);

INSERT INTO `artikel` (`artikelnummer`, `artikel_type`, `naam`, `merk`, `omschrijving`, `is_gratis_artikel`, `aankoopprijs`, `minimale_winstmarge`, `verkoopprijs`, `winstmarge_type`, `voorraad`, `laatste_aankoopdatum`, `minimale_voorraad`, `normale_voorraad`, `minimale_bestelhoeveelheid`, `besteldatum`, `next_backorder_id`, `voorbestellings_prijs`, `voorbestelling_tot_datum`, `leverings_kwartaal`, `voorschot`, `zonder_leveringstijd`, `aantal_in_voorbestelling`, `maximaal_aantal_voorbestellingen`)
VALUES
    ('36435', 'BACKORDER', 'DB BR 243', 'Märklin', 'H0 E-Lok BR 243 DR Ep.IV', FALSE, 329.99, 45.00, 478.49, 'PERCENTAGE', 0, '2025-08-21', 5, 15, 2, '2025-08-21', 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `backorder_lijn` (`id`, `aantal`, `verwachte_datum`, `artikel_artikelnummer`, `backorder_id`)
VALUES
    (1, 10, '2024-06-30', '36435', 1),
    (2, 5, '2024-06-01', '36435', 2),
    (3, 10, '2024-07-15', '36435', 3);