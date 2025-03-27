package be.vdab.modeltreinshop.eip_shop.producten;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArtikelMetNummer implements Artikel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final long id;
    private final String nummer;
    private String naam;
    private String omschrijving;
    private String merk;

    public ArtikelMetNummer(long id, String nummer, String merk, String naam, String omschrijving) {
        List<String> nullParameters = new ArrayList<>();
        if (nummer == null) {
            nullParameters.add("nummer");
        }
        if (merk == null) {
            nullParameters.add("merk");
        }
        if (naam == null) {
            nullParameters.add("naam");
        }
        if (omschrijving == null) {
            nullParameters.add("omschrijving");
        }
        if (!nullParameters.isEmpty()) {
            throw new IllegalArgumentException("De volgende parameters mogen niet null zijn: " + String.join(", ", nullParameters));
        } else {
            List<String> blankParameters = new ArrayList<>();
            if (nummer.trim().isBlank()) {
                blankParameters.add("nummer");
            }
            if (merk.trim().isBlank()) {
                blankParameters.add("merk");
            }
            if (naam.trim().isBlank()) {
                blankParameters.add("naam");
            }
            if (omschrijving.trim().isBlank()) {
                blankParameters.add("omschrijving");
            }
            if (!blankParameters.isEmpty()) {
                throw new IllegalArgumentException("De volgende parameters mogen niet leeg zijn: " + String.join(", ", blankParameters));
            } else {
                this.id = id;
                this.nummer = nummer;
                this.naam = naam;
                this.omschrijving = omschrijving;
                this.merk = merk;
            }
        }
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getNummer() {
        return nummer;
    }

    @Override
    public String getNaam() {
        return naam;
    }

    @Override
    public void setNaam(String naam) {
        if (naam == null || naam.isBlank()) {
            throw new IllegalArgumentException("Naam mag niet null of leeg zijn.");
        }
        this.naam = naam;
    }

    @Override
    public String getOmschrijving() {
        return omschrijving;
    }

    @Override
    public void setOmschrijving(String omschrijving) {
        if (omschrijving == null || omschrijving.isBlank()) {
            throw new IllegalArgumentException("Omschrijving mag niet null of leeg zijn.");
        }
        this.omschrijving = omschrijving;
    }

    @Override
    public String getMerk() {
        return merk;
    }

    @Override
    public void setMerk(String merk) {
        if (merk == null || merk.isBlank()) {
            throw new IllegalArgumentException("Merk mag niet null of leeg zijn.");
        }
        this.merk = merk;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ArtikelMetNummer that)) {
            return false;
        } else {
            return id == that.id && nummer.equals(that.nummer);
        }
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + nummer.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ArtikelMetNummer{" +
               "id=" + id +
               ", nummer='" + nummer + '\'' +
               ", naam='" + naam + '\'' +
               ", omschrijving='" + omschrijving + '\'' +
               ", merk='" + merk + '\'' +
               '}';
    }

}
