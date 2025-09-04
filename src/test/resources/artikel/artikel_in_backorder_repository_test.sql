-- artikel_in_backorder_repository_test.sql

INSERT INTO `artikel` (`artikelnummer`, `artikel_type`, `naam`, `merk`, `omschrijving`, `is_gratis_artikel`, `aankoopprijs`, `minimale_winstmarge`, `verkoopprijs`, `winstmarge_type`, `voorraad`, `laatste_aankoopdatum`, `minimale_voorraad`, `normale_voorraad`, `minimale_bestelhoeveelheid`, `besteldatum`, `next_backorder_id`, `voorbestellings_prijs`, `voorbestelling_tot_datum`, `leverings_kwartaal`, `voorschot`, `zonder_leveringstijd`, `aantal_in_voorbestelling`, `maximaal_aantal_voorbestellingen`)
VALUES
    ('58469', 'BACKORDER', 'PIKO BR 112', 'PIKO', 'BR 112 DB AG Ep.VI', FALSE, 189.99, 35.00, 256.49, 'PERCENTAGE', 1, '2025-08-21', 5, 12, 2, '2025-08-21', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `backorder_lijn` (`id`, `aantal`, `verwachte_datum`, `artikel_artikelnummer`, `backorder_id`)
VALUES
    (1, 8, '2024-07-31', '58469', 1);