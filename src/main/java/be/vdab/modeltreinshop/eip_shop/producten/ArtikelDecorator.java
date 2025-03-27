package be.vdab.modeltreinshop.eip_shop.producten;

public abstract class ArtikelDecorator implements Artikel {
    private final Artikel gedecoreerdArtikel;

    protected ArtikelDecorator(Artikel artikel) {
        if (artikel == null) {
            throw new IllegalArgumentException("Artikel mag niet null zijn.");
        }
        this.gedecoreerdArtikel = artikel;
    }

    @Override
    public long getId() {
        return gedecoreerdArtikel.getId();
    }

    @Override
    public String getNummer() {
        return gedecoreerdArtikel.getNummer();
    }

    @Override
    public String getNaam() {
        return gedecoreerdArtikel.getNaam();
    }

    @Override
    public String getOmschrijving() {
        return gedecoreerdArtikel.getOmschrijving();
    }

    @Override
    public String getMerk() {
        return gedecoreerdArtikel.getMerk();
    }
}

