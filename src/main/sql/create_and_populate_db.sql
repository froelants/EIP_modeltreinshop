-- create_and_populate_db.sql

DROP DATABASE IF EXISTS `modeltreinshop`;
CREATE DATABASE `modeltreinshop`;
USE `modeltreinshop`;

-- CREATE TABLES ZONDER KEYS EN CONSTRAINTS
-- Create superclass table
CREATE TABLE `artikel` (
                           `artikelnummer` VARCHAR(255) NOT NULL,
                           `naam` VARCHAR(255) NOT NULL,
                           `merk` VARCHAR(255) NOT NULL,
                           `omschrijving` TEXT NOT NULL,
                           `kortings_prijs` DECIMAL(19, 2),
                           `korting_tot_datum` DATE,
                           `aankoopprijs` DECIMAL(19, 2) NOT NULL,
                           `is_gratis_artikel` BIT(1),
                           `minimale_winstmarge` DECIMAL(19, 2) NOT NULL,
                           `verkoopprijs` DECIMAL(19, 2) NOT NULL,
                           `winstmarge_type` VARCHAR(255) NOT NULL
);

-- Create subclass tables
CREATE TABLE `artikel_in_voorraad` (
                                       `artikelnummer` VARCHAR(255) NOT NULL,
                                       `voorraad` INT,
                                       `laatste_aankoopdatum` DATE
);

CREATE TABLE `courant_artikel` (
                                   `artikelnummer` VARCHAR(255) NOT NULL,
                                   `minimale_voorraad` INT,
                                   `normale_voorraad` INT,
                                   `minimale_bestelhoeveelheid` INT
);

CREATE TABLE `artikel_in_backorder` (
                                        `artikelnummer` VARCHAR(255) NOT NULL,
                                        `besteldatum` DATE,
                                        `next_backorder_id` INT
);

CREATE TABLE `artikel_in_voorbestelling` (
                                             `artikelnummer` VARCHAR(255) NOT NULL,
                                             `voorbestellings_prijs` DECIMAL(19, 2),
                                             `voorbestelling_tot_datum` DATE,
                                             `leverings_kwartaal` VARCHAR(255),
                                             `voorschot` DECIMAL(19, 2),
                                             `zonder_leveringstijd` BIT(1),
                                             `aantal_in_voorbestelling` INT,
                                             `maximaal_aantal_voorbestellingen` INT
);

-- Auxiliary tables
CREATE TABLE `artikel_afbeeldingen` (
                                        `artikelnummer` VARCHAR(255) NOT NULL,
                                        `afbeelding_url` VARCHAR(255)
);

CREATE TABLE `backorder_lijn` (
                                  `id` BIGINT NOT NULL,
                                  `aantal` INT NOT NULL,
                                  `verwachte_datum` DATE NOT NULL,
                                  `artikel_artikelnummer` VARCHAR(255) NOT NULL,
                                  `backorder_id` INT NOT NULL
);


-- INSERT DATA
-- Stap 1: Voeg alle artikelen toe aan de supertabel `artikel`
INSERT INTO `artikel` (`artikelnummer`, `naam`, `merk`, `omschrijving`, `is_gratis_artikel`, `aankoopprijs`, `minimale_winstmarge`, `verkoopprijs`, `winstmarge_type`, `kortings_prijs`, `korting_tot_datum`)
VALUES
    ('73141', 'BR 01.10 Dampflok', 'Roco', 'DB BR 01.10 Sound', FALSE, 449.99, 50.00, 674.99, 'PERCENTAGE', NULL, NULL),
    ('43279', 'ICE 4 Basisset', 'Märklin', 'DB ICE 4 7-teilig', FALSE, 599.99, 45.00, 869.99, 'PERCENTAGE', NULL, NULL),
    ('72120', 'NS 1200 Blauw', 'Roco', 'NS 1200 Epoche IV DC Sound', FALSE, 299.99, 40.00, 419.99, 'PERCENTAGE', NULL, NULL),
    ('78755', 'TRAXX BR 186', 'Piko', 'DB Cargo TRAXX BR 186 DC', FALSE, 189.99, 35.00, 256.49, 'PERCENTAGE', NULL, NULL),
    ('71403', 'NS Plan V', 'Piko', 'NS Plan V Geel DC Sound', FALSE, 249.99, 45.00, 362.49, 'PERCENTAGE', NULL, NULL),
    ('73801', 'Re 465 SBB', 'Roco', 'SBB Re 465 Cargo AC Sound', FALSE, 379.99, 40.00, 531.99, 'PERCENTAGE', NULL, NULL),
    ('58672', 'DR VT 18.16', 'Piko', 'DR VT 18.16 SVT 175 DC', FALSE, 549.99, 50.00, 824.99, 'PERCENTAGE', NULL, NULL),
    ('73901', 'BR 103', 'Roco', 'DB BR 103 TEE AC Sound', FALSE, 399.99, 45.00, 579.99, 'PERCENTAGE', NULL, NULL),
    ('72233', 'NS 2200', 'Roco', 'NS 2200 Geel/Grijs DC', FALSE, 219.99, 40.00, 307.99, 'PERCENTAGE', NULL, NULL),
    ('73456', 'BR 218', 'Märklin', 'DB BR 218 Rot AC Sound', FALSE, 329.99, 35.00, 445.49, 'PERCENTAGE', NULL, NULL),
    ('74123', 'SNCF CC 40100', 'Roco', 'SNCF CC 40100 DC Sound', FALSE, 459.99, 50.00, 689.99, 'PERCENTAGE', NULL, NULL),
    ('71999', 'NS ICM', 'Piko', 'NS ICM-III Geel/Blauw DC', FALSE, 289.99, 45.00, 420.49, 'PERCENTAGE', NULL, NULL),
    ('76543', 'SBB Re 4/4 II', 'Märklin', 'SBB Re 4/4 II AC Sound', FALSE, 369.99, 40.00, 517.99, 'PERCENTAGE', NULL, NULL),
    ('75321', 'ÖBB 1216', 'Roco', 'ÖBB 1216 Railjet DC', FALSE, 279.99, 35.00, 377.99, 'PERCENTAGE', NULL, NULL),
    ('77777', 'BR 101', 'Märklin', 'DB BR 101 AC Sound', FALSE, 419.99, 45.00, 609.49, 'PERCENTAGE', NULL, NULL),
    ('73916', 'BR 193 Vectron DB AG', 'Roco', 'DB BR 193 Electric Locomotive Era VI', FALSE, 259.99, 40.00, 363.99, 'PERCENTAGE', NULL, NULL),
    ('71400', 'ICE 4 Basisset', 'PIKO', 'DB AG ICE 4 7-delig treinstel', FALSE, 399.99, 50.00, 449.99, 'EURO', NULL, NULL),
    ('88487', 'Mini-Club BR 101', 'Märklin', 'Z-schaal elektrische locomotief', FALSE, 189.99, 30.00, 246.99, 'PERCENTAGE', NULL, NULL),
    ('99999', 'Catalogus 2024', 'Märklin', 'Märklin volledige catalogus 2024', TRUE, 0.00, 0.00, 0.00, 'PERCENTAGE', NULL, NULL),
    ('36435', 'DB BR 243', 'Märklin', 'H0 E-Lok BR 243 DR Ep.IV', FALSE, 329.99, 45.00, 478.49, 'PERCENTAGE', NULL, NULL),
    ('73127', 'NS 1600', 'Roco', 'Elektrische locomotief NS 1600', FALSE, 249.99, 40.00, 289.99, 'EURO', NULL, NULL),
    ('58469', 'PIKO BR 112', 'PIKO', 'BR 112 DB AG Ep.VI', FALSE, 189.99, 35.00, 256.49, 'PERCENTAGE', NULL, NULL),
    ('73141-2', 'BR 01.10 Schnellzug-Dampflok', 'Roco', 'DB BR 01.10 mit Ölfeuerung Sound', FALSE, 449.99, 50.00, 674.99, 'PERCENTAGE', NULL, NULL),
    ('43279-2', 'ICE 4 Basisset', 'Märklin', 'DB ICE 4 Basisgarnitur 7-teilig', FALSE, 599.99, 45.00, 869.99, 'PERCENTAGE', NULL, NULL);

-- Stap 2: Voeg alle artikelen die een voorraad hebben in de `artikel_in_voorraad` tabel
INSERT INTO `artikel_in_voorraad` (`artikelnummer`, `voorraad`, `laatste_aankoopdatum`)
VALUES
    ('73141', 8, '2025-08-21'),
    ('43279', 15, '2025-08-21'),
    ('72120', 5, '2025-08-21'),
    ('78755', 3, '2025-08-21'),
    ('71403', 12, '2025-08-21'),
    ('73801', 7, '2025-08-21'),
    ('58672', 20, '2025-08-21'),
    ('73901', 4, '2025-08-21'),
    ('72233', 6, '2025-08-21'),
    ('73456', 9, '2025-08-21'),
    ('74123', 2, '2025-08-21'),
    ('71999', 18, '2025-08-21'),
    ('76543', 11, '2025-08-21'),
    ('75321', 13, '2025-08-21'),
    ('77777', 16, '2025-08-21'),
    ('73916', 10, '2025-08-21'),
    ('71400', 5, '2025-08-21'),
    ('88487', 15, '2025-08-21'),
    ('99999', 100, '2025-08-21'),
    ('36435', 0, '2025-08-21'),
    ('73127', 2, '2025-08-21'),
    ('58469', 1, '2025-08-21');

-- Stap 3: Voeg nu de courante artikelen toe aan hun eigen tabel
INSERT INTO `courant_artikel` (`artikelnummer`, `minimale_voorraad`, `normale_voorraad`, `minimale_bestelhoeveelheid`)
VALUES
    ('73141', 10, 25, 5),
    ('43279', 12, 30, 6),
    ('72120', 8, 20, 4),
    ('78755', 5, 15, 5),
    ('71403', 10, 20, 5),
    ('73801', 10, 25, 5),
    ('58672', 15, 35, 10),
    ('73901', 8, 20, 4),
    ('72233', 5, 15, 5),
    ('73456', 10, 25, 5),
    ('74123', 5, 15, 5),
    ('71999', 15, 30, 8),
    ('76543', 10, 25, 5),
    ('75321', 10, 20, 5),
    ('77777', 15, 35, 10),
    ('36435', 5, 15, 2),
    ('73127', 3, 10, 1),
    ('58469', 5, 12, 2);

-- Stap 4: Voeg backorder artikelen toe aan hun eigen tabel
INSERT INTO `artikel_in_backorder` (`artikelnummer`, `besteldatum`, `next_backorder_id`)
VALUES
    ('36435', '2025-08-21', 3),
    ('73127', '2025-08-21', 2),
    ('58469', '2025-08-21', 2);

-- Stap 5: Voeg voorbestellingsartikelen toe aan hun eigen tabel
INSERT INTO `artikel_in_voorbestelling` (`artikelnummer`, `voorbestellings_prijs`, `voorbestelling_tot_datum`, `leverings_kwartaal`, `voorschot`, `zonder_leveringstijd`, `aantal_in_voorbestelling`, `maximaal_aantal_voorbestellingen`)
VALUES
    ('73141-2', 599.99, '2024-06-30', '2024-Q3', 100.00, FALSE, 0, NULL),
    ('43279-2', 799.99, '2024-05-31', '2024-Q2', 150.00, FALSE, 0, NULL);

-- Stap 6: Voeg de overige gerelateerde data toe aan de auxiliary tables
INSERT INTO `artikel_afbeeldingen` (`artikelnummer`, `afbeelding_url`)
VALUES
    ('73141', '73141-1.jpg'), ('73141', '73141-2.jpg'),
    ('43279', '43279.jpg'),
    ('72120', '72120.jpg'),
    ('78755', '78755.jpg'),
    ('71403', '71403.jpg'),
    ('73801', '73801.jpg'),
    ('58672', '58672.jpg'),
    ('73901', '73901.jpg'),
    ('72233', '72233.jpg'),
    ('73456', '73456.jpg'),
    ('74123', '74123.jpg'),
    ('71999', '71999.jpg'),
    ('76543', '76543.jpg'),
    ('75321', '75321.jpg'),
    ('77777', '77777.jpg'),
    ('73916', '73916-1.jpg'), ('73916', '73916-2.jpg'),
    ('71400', '71400.jpg'),
    ('88487', '88487-1.jpg'), ('88487', '88487-2.jpg'),
    ('99999', 'catalog.jpg'),
    ('36435', '36435-1.jpg'), ('36435', '36435-2.jpg'),
    ('73127', '73127.jpg'),
    ('58469', '58469-1.jpg'), ('58469', '58469-2.jpg'),
    ('73141-2', '73141-1.jpg'), ('73141-2', '73141-2.jpg'),
    ('43279-2', '43279.jpg');

INSERT INTO `backorder_lijn` (`id`, `aantal`, `verwachte_datum`, `artikel_artikelnummer`, `backorder_id`)
VALUES
    (1, 10, '2024-06-30', '36435', 1),
    (2, 5, '2024-05-15', '73127', 1),
    (3, 8, '2024-07-31', '58469', 1),
    (4, 5, '2024-06-01', '36435', 2),
    (5, 10, '2024-07-15', '36435', 3);


-- VOEG NU PAS DE KEYS EN CONSTRAINTS TOE
ALTER TABLE `artikel` ADD PRIMARY KEY (`artikelnummer`);
ALTER TABLE `artikel_in_voorraad` ADD PRIMARY KEY (`artikelnummer`);
ALTER TABLE `courant_artikel` ADD PRIMARY KEY (`artikelnummer`);
ALTER TABLE `artikel_in_backorder` ADD PRIMARY KEY (`artikelnummer`);
ALTER TABLE `artikel_in_voorbestelling` ADD PRIMARY KEY (`artikelnummer`);
ALTER TABLE `artikel_afbeeldingen` ADD FOREIGN KEY (`artikelnummer`) REFERENCES `artikel`(`artikelnummer`);
ALTER TABLE `backorder_lijn` ADD PRIMARY KEY (`id`);
ALTER TABLE `backorder_lijn` MODIFY `id` BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `backorder_lijn` ADD FOREIGN KEY (`artikel_artikelnummer`) REFERENCES `artikel_in_backorder`(`artikelnummer`);
ALTER TABLE `artikel_in_voorraad` ADD FOREIGN KEY (`artikelnummer`) REFERENCES `artikel`(`artikelnummer`);
ALTER TABLE `courant_artikel` ADD FOREIGN KEY (`artikelnummer`) REFERENCES `artikel_in_voorraad`(`artikelnummer`);
ALTER TABLE `artikel_in_backorder` ADD FOREIGN KEY (`artikelnummer`) REFERENCES `courant_artikel`(`artikelnummer`);
ALTER TABLE `artikel_in_voorbestelling` ADD FOREIGN KEY (`artikelnummer`) REFERENCES `artikel`(`artikelnummer`);