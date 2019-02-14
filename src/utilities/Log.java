package utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private String messaggio;
    private String timestamp;

    public Log(String messaggio) {
        this.setMessaggio(messaggio);
        this.setTimestamp(new SimpleDateFormat(
                "yyyy/MM/dd - HH:mm").format(new Date()));
    }

    public StringProperty getMessaggioProperty() {
        StringProperty spMessaggio = new SimpleStringProperty(getMessaggio());
        return spMessaggio;
    }

    public StringProperty getTimestampProperty() {
        StringProperty spTimestamp = new SimpleStringProperty(getTimestamp());
        return spTimestamp;
    }

    public String toString() {

        return this.getMessaggio() + " - " + this.getTimestamp();
    }


    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
