-- artikel_in_voorbestelling_repository_test.sql

INSERT INTO `artikel` (`artikelnummer`, `artikel_type`, `naam`, `merk`, `omschrijving`, `is_gratis_artikel`, `aankoopprijs`, `minimale_winstmarge`, `verkoopprijs`, `winstmarge_type`, `voorraad`, `laatste_aankoopdatum`, `minimale_voorraad`, `normale_voorraad`, `minimale_bestelhoeveelheid`, `besteldatum`, `next_backorder_id`, `voorbestellings_prijs`, `voorbestelling_tot_datum`, `leverings_kwartaal`, `voorschot`, `zonder_leveringstijd`, `aantal_in_voorbestelling`, `maximaal_aantal_voorbestellingen`)
VALUES
    ('43279-2', 'VOORBESTELLING', 'ICE 4 Basisset', 'Märklin', 'DB ICE 4 Basisgarnitur 7-teilig', FALSE, 599.99, 45.00, 869.99, 'PERCENTAGE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 799.99, '2024-05-31', '2024-Q2', 150.00, FALSE, 0, NULL);