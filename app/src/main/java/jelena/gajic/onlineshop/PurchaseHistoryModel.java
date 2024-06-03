package jelena.gajic.onlineshop;

public class PurchaseHistoryModel {
    public enum State {DELIVERED, CANCELLED, WAITING_FOR_DELIVERY}
    private State status;
    private int ukupnaCena;
    private String datum;

    public PurchaseHistoryModel(State status, int ukupnaCena, String datum) {
        this.status = status;
        this.ukupnaCena = ukupnaCena;
        this.datum = datum;
    }


    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status=status;
    }

    public int getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(int ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}
