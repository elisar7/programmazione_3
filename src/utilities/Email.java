package utilities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Email implements Serializable {


    private int ID;
    private String mittente;
    private ArrayList<String> destinatari;
    private String oggetto;
    private String testo;
    private String timestamp;

    public Email(String mittente, ArrayList<String> destinatari,
                 String oggetto, String testo, String timestamp) {
        this.setMittente(mittente);
        this.setDestinatari(destinatari);
        this.setOggetto(oggetto);
        this.setTesto(testo);
        this.setTimestamp(timestamp);
    }

    public Email(int ID, String mittente, ArrayList<String> destinatari,
                 String oggetto, String testo, String timestamp) {
        this.setID(ID);
        this.setMittente(mittente);
        this.setDestinatari(destinatari);
        this.setOggetto(oggetto);
        this.setTesto(testo);
        this.setTimestamp(timestamp);
    }

    public Email(String mittente) {
        this.setMittente(mittente);
        this.setDestinatari(new ArrayList<>());
        this.setOggetto("");
        this.setTesto("");
        this.setTimestamp(new SimpleDateFormat(
                "yyyy/MM/dd - HH:mm").format(new Date()));
    }

    public String toString() {
        String result = getMittente() + " % ";
        for (int i = 0; i < getDestinatari().size(); i++) {
            if (i < getDestinatari().size() - 1)
                result += getDestinatari().get(i) + ", ";
            else
                result += getDestinatari().get(i);
        }
        result += " % " + getOggetto();
        result += " % " + getTesto();
        result += " % " + getTimestamp();
        return result;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMittente() {
        return mittente;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public ArrayList<String> getDestinatari() {
        return destinatari;
    }

    public void setDestinatari(ArrayList<String> destinatari) {
        this.destinatari = destinatari;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
