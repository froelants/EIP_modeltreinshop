-- artikel_in_voorraad_repository_test.sql

INSERT INTO `artikel` (`artikelnummer`, `artikel_type`, `naam`, `merk`, `omschrijving`, `is_gratis_artikel`, `aankoopprijs`, `minimale_winstmarge`, `verkoopprijs`, `winstmarge_type`, `voorraad`, `laatste_aankoopdatum`, `minimale_voorraad`, `normale_voorraad`, `minimale_bestelhoeveelheid`, `besteldatum`, `next_backorder_id`, `voorbestellings_prijs`, `voorbestelling_tot_datum`, `leverings_kwartaal`, `voorschot`, `zonder_leveringstijd`, `aantal_in_voorbestelling`, `maximaal_aantal_voorbestellingen`)
VALUES
    ('73916', 'VOORRAAD', 'BR 193 Vectron DB AG', 'Roco', 'DB BR 193 Electric Locomotive Era VI', FALSE, 259.99, 40.00, 363.99, 'PERCENTAGE', 10, '2025-08-21', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
