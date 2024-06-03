package jelena.gajic.onlineshop;
import android.graphics.drawable.Drawable;
import android.widget.Button;

public class Model {
    private Drawable slika;
    private String naziv;
    private String cena;
    private String kategorija;

    public Model(Drawable slika, String naziv, String cena, String kategorija) {
        this.slika = slika;
        this.naziv = naziv;
        this.cena = cena;
        this.kategorija = kategorija;
    }

    public Drawable getSlika() {
        return slika;
    }

    public void setSlika(Drawable slika) {
        this.slika = slika;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getCena() {
        return cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }
}