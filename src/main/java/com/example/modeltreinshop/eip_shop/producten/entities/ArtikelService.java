package com.example.modeltreinshop.eip_shop.producten.entities;

@Service
public class ArtikelService {

    private final ArtikelRepository artikelRepository;
    private final ArtikelInVoorraadRepository artikelInVoorraadRepository;
    private final CourantArtikelRepository courantArtikelRepository;
    private final ArtikelInVoorbestellingRepository artikelInVoorbestellingRepository;
    private final ArtikelInBackorderRepository artikelInBackorderRepository;
    private final WinstmargeTypeRepository winstmargeTypeRepository;

    public ArtikelService(ArtikelRepository artikelRepository,
                          ArtikelInVoorraadRepository artikelInVoorraadRepository,
                          CourantArtikelRepository courantArtikelRepository,
                          ArtikelInVoorbestellingRepository artikelInVoorbestellingRepository,
                          ArtikelInBackorderRepository artikelInBackorderRepository,
                          WinstmargeTypeRepository winstmargeTypeRepository) {
        this.artikelRepository = artikelRepository;
        this.artikelInVoorraadRepository = artikelInVoorraadRepository;
        this.courantArtikelRepository = courantArtikelRepository;
        this.artikelInVoorbestellingRepository = artikelInVoorbestellingRepository;
        this.artikelInBackorderRepository = artikelInBackorderRepository;
        this.winstmargeTypeRepository = winstmargeTypeRepository;
    }

    // Voorbeeld: Een artikel ophalen en proberen te decoreren
    public Artikel getGedecoreerdArtikel(Long id) {
        Optional<Artikel> optArtikel = artikelRepository.findById(id);
        if (optArtikel.isEmpty()) {
            return null; // Of throw exception
        }
        Artikel artikel = optArtikel.get();

        // Nu de "decoraties" ophalen en de objecten bouwen
        // Dit is waar je de business logica toepast om de decorator-hiërarchie te reconstrueren.
        // JPA haalt de componenten op; jij zet ze in elkaar.

        Optional<ArtikelInVoorraad> optAiv = artikelInVoorraadRepository.findByArtikelId(artikel.getId());
        if (optAiv.isPresent()) {
            ArtikelInVoorraad artikelInVoorraad = optAiv.get();
            // Hier zou je je ArtikelInVoorraad object maken met de PrijsComponent
            // en dan verder decoreren

            Optional<CourantArtikel> optCa = courantArtikelRepository.findByArtikelInVoorraad_ArtikelId(artikelInVoorraad.getArtikelId());
            if (optCa.isPresent()) {
                CourantArtikel courantArtikel = optCa.get();
                // Nu heb je een CourantArtikel, die een ArtikelInVoorraad 'is'.
            }
        }

        Optional<ArtikelInVoorbestelling> optAivb = artikelInVoorbestellingRepository.findByArtikelId(artikel.getId());
        if (optAivb.isPresent()) {
            ArtikelInVoorbestelling artikelInVoorbestelling = optAivb.get();
            // ...
        }

        Optional<ArtikelInBackorder> optAib = artikelInBackorderRepository.findByArtikelId(artikel.getId());
        if (optAib.isPresent()) {
            ArtikelInBackorder artikelInBackorder = optAib.get();
            // ...
        }

        return artikel; // Of het daadwerkelijk gedecoreerde object
    }

    // Voorbeeld: Een nieuw artikel opslaan
    @Transactional
    public Artikel createArtikel(String nummer, String naam, String merk, boolean eenmalig) {
        Artikel artikel = new Artikel();
        artikel.setNummer(nummer);
        artikel.setNaam(naam);
        artikel.setMerk(merk);
        artikel.setEenmaligArtikel(eenmalig);
        return artikelRepository.save(artikel);
    }
}