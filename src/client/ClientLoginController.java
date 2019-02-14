package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import utilities.AlertUtility;
import utilities.ServerRemoteModel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class ClientLoginController {

    private ServerRemoteModel server_remote;
    private ClientModel client_model;

    //Login View
    @FXML
    private ObservableList<String> indirizzi =
            FXCollections.observableArrayList(
                    "utente1@mail.com",
                    "utente2@mail.com",
                    "utente3@mail.com"
            );
    @FXML
    private ComboBox scegli_email;

    @FXML
    public void initialize() {
        this.client_model = new ClientModel();
        scegli_email.setItems(indirizzi);
    }

    @FXML
    private void login(ActionEvent event) {
        try {
            server_remote = (ServerRemoteModel) Naming.lookup(
                    "rmi://127.0.0.1:2000/server");
            if (scegli_email.getSelectionModel().isEmpty()) {
                AlertUtility.alertError("Seleziona il tuo indirizzo email");
            } else {
                String indirizzo = (String) scegli_email.getSelectionModel().getSelectedItem();
                client_model.setServerModel(server_remote);
                client_model.setAddress(indirizzo);
                if (client_model.setUpMailbox()) {
                    server_remote.addLog(indirizzo + " si è connesso.");
                    showMailboxView(event);
                } else {
                    AlertUtility.alertError("Impossibile collegarsi a questo account:" +
                            " qualcun altro è già connesso.");
                }
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            AlertUtility.alertError("Non sono riuscito a connettermi al server.");
        }
    }

    private void showMailboxView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("MailboxView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Mailbox");
            stage.setScene(new Scene(root, 1200, 800));
            ((Node) (event.getSource())).getScene().getWindow().hide();
            ClientMailboxController mailbox_controller = loader.getController();
            mailbox_controller.setUpMailbox(server_remote, client_model);
            stage.setTitle(client_model.getAddress());
            stage.show();
            stage.setOnCloseRequest(e -> mailbox_controller.shutdown());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
    }

}
