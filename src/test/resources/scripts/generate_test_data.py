import csv
from itertools import product

# Invalid string values
string_invalids = [None, "", " ", "   ", "\n", "\t"]
# Invalid numeric values
numeric_invalids = [-1.0, 0.0, 999999999.99]
# Invalid date values
month_invalids = [0, 13, -1]
year_invalids = [2023, 9999]

def generate_test_cases():
    with open('src/test/resources/invalid_voorbestellingen.csv', 'a', newline='') as f:
        writer = csv.writer(f)

        # First check if file is empty to add headers only if needed
        f.seek(0, 2)  # Go to end of file
        if f.tell() == 0:  # If file is empty, write headers
            writer.writerow(['artikelnummer', 'naam', 'merk', 'aankoopprijs', 'winstmarge',
                           'winstmargeType', 'verkoopprijs', 'voorbestellingsprijs',
                           'omschrijving', 'voorschot', 'leveringsmaand', 'leveringsjaar',
                           'gratisArtikel', 'kortingsPrijs', 'kortingTotDatum'])

        # Generate string field combinations
        for invalid_strings in product(string_invalids, repeat=4):
            writer.writerow([*invalid_strings, 159.99, 25.0, 'PERCENTAGE', 199.99,
                           189.99, 37.99, 6, 2024, False, None, None])

        # Generate price field combinations
        for invalid_prices in product(numeric_invalids, repeat=5):
            writer.writerow(['TEST001', 'Test Naam', 'Test Merk', *invalid_prices,
                           'PERCENTAGE', 'Test Omschrijving', 6, 2024, False, None, None])

        # Generate date field combinations
        for month in month_invalids:
            for year in year_invalids:
                writer.writerow(['TEST001', 'Test Naam', 'Test Merk', 159.99, 25.0,
                               'PERCENTAGE', 199.99, 189.99, 'Test Omschrijving',
                               37.99, month, year, False, None, None])

        # Generate special combinations
        special_cases = [
            # Gratis artikel with non-zero prices
            ['TEST001', 'Test Naam', 'Test Merk', 159.99, 25.0, 'PERCENTAGE',
             199.99, 189.99, 'Test Omschrijving', 37.99, 6, 2024, True, None, None],
            # Invalid korting combinations
            ['TEST001', 'Test Naam', 'Test Merk', 159.99, 25.0, 'PERCENTAGE',
             199.99, 189.99, 'Test Omschrijving', 37.99, 6, 2024, False, 50.0, '2023-12-31'],
            # Voorbestellingsprijs lower than aankoopprijs
            ['TEST001', 'Test Naam', 'Test Merk', 159.99, 25.0, 'PERCENTAGE',
             199.99, 150.00, 'Test Omschrijving', 37.99, 6, 2024, False, None, None],
        ]
        writer.writerows(special_cases)