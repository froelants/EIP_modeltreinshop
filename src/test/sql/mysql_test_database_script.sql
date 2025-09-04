-- create_and_populate_db.sql

DROP DATABASE IF EXISTS `modeltreinshop__test`;
CREATE DATABASE `modeltreinshop_test`;
USE `modeltreinshop_test`;

-- Create Artikel table with SINGLE_TABLE inheritance
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
    -- ArtikelInVoorraad fields
                           `voorraad` INT,
                           `laatste_aankoopdatum` DATE,
    -- CourantArtikel fields
                           `minimale_voorraad` INT,
                           `normale_voorraad` INT,
                           `minimale_bestelhoeveelheid` INT,
    -- ArtikelInBackorder fields
                           `besteldatum` DATE,
                           `next_backorder_id` INT,
    -- ArtikelInVoorbestelling fields
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

-- Foreign keys
ALTER TABLE `artikel_afbeeldingen` ADD CONSTRAINT `FK_artikel_afbeeldingen_artikel` FOREIGN KEY (`artikelnummer`) REFERENCES `artikel`(`artikelnummer`);
ALTER TABLE `backorder_lijn` ADD CONSTRAINT `FK_backorder_lijn_artikel` FOREIGN KEY (`artikel_artikelnummer`) REFERENCES `artikel`(`artikelnummer`);

-- Insert data into `artikel` table
INSERT INTO `artikel` (`artikelnummer`, `artikel_type`, `naam`, `merk`, `omschrijving`, `is_gratis_artikel`, `aankoopprijs`, `minimale_winstmarge`, `verkoopprijs`, `winstmarge_type`, `voorraad`, `laatste_aankoopdatum`, `minimale_voorraad`, `normale_voorraad`, `minimale_bestelhoeveelheid`, `besteldatum`, `next_backorder_id`, `voorbestellings_prijs`, `voorbestelling_tot_datum`, `leverings_kwartaal`, `voorschot`, `zonder_leveringstijd`, `aantal_in_voorbestelling`, `maximaal_aantal_voorbestellingen`)
VALUES
-- Courant artikelen
('73141', 'COURANT', 'BR 01.10 Dampflok', 'Roco', 'DB BR 01.10 Sound', FALSE, 449.99, 50.00, 674.99, 'PERCENTAGE', 8, '2025-08-21', 10, 25, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('43279', 'COURANT', 'ICE 4 Basisset', 'Märklin', 'DB ICE 4 7-teilig', FALSE, 599.99, 45.00, 869.99, 'PERCENTAGE', 15, '2025-08-21', 12, 30, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('72120', 'COURANT', 'NS 1200 Blauw', 'Roco', 'NS 1200 Epoche IV DC Sound', FALSE, 299.99, 40.00, 419.99, 'PERCENTAGE', 5, '2025-08-21', 8, 20, 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('78755', 'COURANT', 'TRAXX BR 186', 'Piko', 'DB Cargo TRAXX BR 186 DC', FALSE, 189.99, 35.00, 256.49, 'PERCENTAGE', 3, '2025-08-21', 5, 15, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('71403', 'COURANT', 'NS Plan V', 'Piko', 'NS Plan V Geel DC Sound', FALSE, 249.99, 45.00, 362.49, 'PERCENTAGE', 12, '2025-08-21', 10, 20, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('73801', 'COURANT', 'Re 465 SBB', 'Roco', 'SBB Re 465 Cargo AC Sound', FALSE, 379.99, 40.00, 531.99, 'PERCENTAGE', 7, '2025-08-21', 10, 25, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('58672', 'COURANT', 'DR VT 18.16', 'Piko', 'DR VT 18.16 SVT 175 DC', FALSE, 549.99, 50.00, 824.99, 'PERCENTAGE', 20, '2025-08-21', 15, 35, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('73901', 'COURANT', 'BR 103', 'Roco', 'DB BR 103 TEE AC Sound', FALSE, 399.99, 45.00, 579.99, 'PERCENTAGE', 4, '2025-08-21', 8, 20, 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('72233', 'COURANT', 'NS 2200', 'Roco', 'NS 2200 Geel/Grijs DC', FALSE, 219.99, 40.00, 307.99, 'PERCENTAGE', 6, '2025-08-21', 5, 15, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('73456', 'COURANT', 'BR 218', 'Märklin', 'DB BR 218 Rot AC Sound', FALSE, 329.99, 35.00, 445.49, 'PERCENTAGE', 9, '2025-08-21', 10, 25, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('74123', 'COURANT', 'SNCF CC 40100', 'Roco', 'SNCF CC 40100 DC Sound', FALSE, 459.99, 50.00, 689.99, 'PERCENTAGE', 2, '2025-08-21', 5, 15, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('71999', 'COURANT', 'NS ICM', 'Piko', 'NS ICM-III Geel/Blauw DC', FALSE, 289.99, 45.00, 420.49, 'PERCENTAGE', 18, '2025-08-21', 15, 30, 8, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('76543', 'COURANT', 'SBB Re 4/4 II', 'Märklin', 'SBB Re 4/4 II AC Sound', FALSE, 369.99, 40.00, 517.99, 'PERCENTAGE', 11, '2025-08-21', 10, 25, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('75321', 'COURANT', 'ÖBB 1216', 'Roco', 'ÖBB 1216 Railjet DC', FALSE, 279.99, 35.00, 377.99, 'PERCENTAGE', 13, '2025-08-21', 10, 20, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('77777', 'COURANT', 'BR 101', 'Märklin', 'DB BR 101 AC Sound', FALSE, 419.99, 45.00, 609.49, 'PERCENTAGE', 16, '2025-08-21', 15, 35, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),

-- ArtikelInVoorraad
('73916', 'VOORRAAD', 'BR 193 Vectron DB AG', 'Roco', 'DB BR 193 Electric Locomotive Era VI', FALSE, 259.99, 40.00, 363.99, 'PERCENTAGE', 10, '2025-08-21', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('71400', 'VOORRAAD', 'ICE 4 Basisset', 'PIKO', 'DB AG ICE 4 7-delig treinstel', FALSE, 399.99, 50.00, 449.99, 'EURO', 5, '2025-08-21', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('88487', 'VOORRAAD', 'Mini-Club BR 101', 'Märklin', 'Z-schaal elektrische locomotief', FALSE, 189.99, 30.00, 246.99, 'PERCENTAGE', 15, '2025-08-21', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('99999', 'VOORRAAD', 'Catalogus 2024', 'Märklin', 'Märklin volledige catalogus 2024', TRUE, 0.00, 0.00, 0.00, 'PERCENTAGE', 100, '2025-08-21', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),

-- ArtikelInBackorder
('36435', 'BACKORDER', 'DB BR 243', 'Märklin', 'H0 E-Lok BR 243 DR Ep.IV', FALSE, 329.99, 45.00, 478.49, 'PERCENTAGE', 0, '2025-08-21', 5, 15, 2, '2025-08-21', 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('73127', 'BACKORDER', 'NS 1600', 'Roco', 'Elektrische locomotief NS 1600', FALSE, 249.99, 40.00, 289.99, 'EURO', 2, '2025-08-21', 3, 10, 1, '2025-08-21', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('58469', 'BACKORDER', 'PIKO BR 112', 'PIKO', 'BR 112 DB AG Ep.VI', FALSE, 189.99, 35.00, 256.49, 'PERCENTAGE', 1, '2025-08-21', 5, 12, 2, '2025-08-21', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL),

-- ArtikelInVoorbestelling
('73141-2', 'VOORBESTELLING', 'BR 01.10 Schnellzug-Dampflok', 'Roco', 'DB BR 01.10 mit Ölfeuerung Sound', FALSE, 449.99, 50.00, 674.99, 'PERCENTAGE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 599.99, '2024-06-30', '2024-Q3', 100.00, FALSE, 0, NULL),
('43279-2', 'VOORBESTELLING', 'ICE 4 Basisset', 'Märklin', 'DB ICE 4 Basisgarnitur 7-teilig', FALSE, 599.99, 45.00, 869.99, 'PERCENTAGE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 799.99, '2024-05-31', '2024-Q2', 150.00, FALSE, 0, NULL);

-- Insert data into `artikel_afbeeldingen` table
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

-- Insert data into `backorder_lijn` table
INSERT INTO `backorder_lijn` (`aantal`, `verwachte_datum`, `artikel_artikelnummer`, `backorder_id`)
VALUES
    (10, '2024-06-30', '36435', 1),
    (5, '2024-05-15', '73127', 1),
    (8, '2024-07-31', '58469', 1),
    (5, '2024-06-01', '36435', 2),
    (10, '2024-07-15', '36435', 3);artikel