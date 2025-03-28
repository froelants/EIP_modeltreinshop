package be.vdab.modeltreinshop.eip_shop.producten;

import be.vdab.modeltreinshop.eip_shop.util.IllegalBlankArgumentException;
import be.vdab.modeltreinshop.eip_shop.util.IllegalNullArgumentException;

import java.io.Serial;
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
            throw new IllegalNullArgumentException(String.join(", ", nullParameters)+" mag niet null zijn: ");
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
                throw new IllegalBlankArgumentException( String.join(", ", blankParameters)+ "mag niet leeg zijn");
            } else {
                this.id = id;
                this.nummer = nummer.trim();
                this.naam = naam.trim();
                this.omschrijving = omschrijving.trim();
                this.merk = merk.trim();
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
        if (naam == null){
            throw new IllegalArgumentException("Omschrijving mag niet leeg zijn.");
        }else {
            if(naam.trim().isBlank()){
                throw new IllegalArgumentException("Omschrijving mag niet null zijn.");
            }
            this.naam = naam.trim();
        }
    }

    @Override
    public String getOmschrijving() {
        return omschrijving.trim();
    }

    @Override
    public void setOmschrijving(String omschrijving) {
        if (omschrijving == null){
            throw new IllegalArgumentException("Omschrijving mag niet leeg zijn.");
        }else {
            if(omschrijving.trim().isBlank()){
                throw new IllegalArgumentException("Omschrijving mag niet null zijn.");
            }
            this.omschrijving = omschrijving.trim();
        }
    }

    @Override
    public String getMerk() {
        return merk;
    }

    @Override
    public void setMerk(String merk) {
        if( merk == null){
            throw new IllegalArgumentException("Omschrijving mag niet leeg zijn.");
        }else {
            if(merk.trim().isBlank()){
                throw new IllegalArgumentException("Omschrijving mag niet null zijn.");
            }
            this.merk = merk.trim();
        }
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
