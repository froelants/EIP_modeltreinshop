-- courant_artikel_repository_test.sql

INSERT INTO `artikel` (`artikelnummer`, `artikel_type`, `naam`, `merk`, `omschrijving`, `is_gratis_artikel`, `aankoopprijs`, `minimale_winstmarge`, `verkoopprijs`, `winstmarge_type`, `voorraad`, `laatste_aankoopdatum`, `minimale_voorraad`, `normale_voorraad`, `minimale_bestelhoeveelheid`, `besteldatum`, `next_backorder_id`, `voorbestellings_prijs`, `voorbestelling_tot_datum`, `leverings_kwartaal`, `voorschot`, `zonder_leveringstijd`, `aantal_in_voorbestelling`, `maximaal_aantal_voorbestellingen`)
VALUES
    ('73141', 'COURANT', 'BR 01.10 Dampflok', 'Roco', 'DB BR 01.10 Sound', FALSE, 449.99, 50.00, 674.99, 'PERCENTAGE', 8, '2025-08-21', 10, 25, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);