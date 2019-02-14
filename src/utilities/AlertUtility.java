package utilities;

import javafx.scene.control.Alert;

public class AlertUtility {
    public static void alertError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static void alertInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operazione effettuata con successo");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
