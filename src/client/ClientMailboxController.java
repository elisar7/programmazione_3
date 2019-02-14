package client;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utilities.AlertUtility;
import utilities.Email;
import utilities.ServerRemoteModel;

import java.io.IOException;
import java.rmi.RemoteException;

public class ClientMailboxController {

    ClientMailCreatorController creator_controller;
    private ServerRemoteModel server_remote;
    private ClientModel client_model;
    @FXML
    private TableView<Email> sentTable;
    @FXML
    private TableColumn<Email, String> sentMittenti;
    @FXML
    private TableColumn<Email, String> sentDestinatari;
    @FXML
    private TableColumn<Email, String> sentOggetti;
    @FXML
    private TableColumn<Email, String> sentTimestamps;
    @FXML
    private TableView<Email> inboxTable;
    @FXML
    private TableColumn<Email, String> inboxMittenti;
    @FXML
    private TableColumn<Email, String> inboxDestinatari;
    @FXML
    private TableColumn<Email, String> inboxOggetti;
    @FXML
    private TableColumn<Email, String> inboxTimestamps;
    @FXML
    private Label mittenteLabel;
    @FXML
    private Label destinatariLabel;
    @FXML
    private Label oggettoLabel;
    @FXML
    private Label timestampLabel;
    @FXML
    private TextArea testoArea;
    @FXML
    private Button rispondi;
    @FXML
    private Button rispondi_tutti;
    @FXML
    private Button inoltra;

    public void setUpMailbox(ServerRemoteModel server_remote, ClientModel client_model) {
        this.server_remote = server_remote;
        this.client_model = client_model;
        inboxTable.setItems(client_model.getInbox());
        sentTable.setItems(client_model.getSent());

        inboxMittenti.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMittente()));
        inboxDestinatari.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDestinatari().toString()));
        inboxOggetti.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOggetto()));
        inboxTimestamps.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTimestamp()));

        sentMittenti.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMittente()));
        sentDestinatari.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDestinatari().toString()));
        sentOggetti.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOggetto()));
        sentTimestamps.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTimestamp()));

        showEmailDetails(null);
        inboxTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showEmailDetails(newValue));
        sentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showEmailDetails(newValue));

        Thread syncMail = new Thread(new CheckMail());
        syncMail.setDaemon(true);
        syncMail.start();
    }

    private void showEmailDetails(Email email) {
        if (email != null) {
            mittenteLabel.setText(email.getMittente());
            destinatariLabel.setText(email.getDestinatari().toString());
            oggettoLabel.setText(email.getOggetto());
            timestampLabel.setText(email.getTimestamp());
            testoArea.setText(email.getTesto());
        } else {
            mittenteLabel.setText("Mittente");
            destinatariLabel.setText("Destinatari");
            oggettoLabel.setText("Oggetto");
            timestampLabel.setText("Timestamp");
            testoArea.setText("Testo");
        }
    }

    @FXML
    private void inviaNuovaMail(ActionEvent event) {
        Stage stage = setCreator();
        creator_controller.setUpMail();
        stage.show();
    }

    @FXML
    private void inviaMail(ActionEvent event) {
        Email email = null;
        if (!sentTable.getSelectionModel().isEmpty()) {
            email = sentTable.getSelectionModel().getSelectedItem();
            Stage stage = setCreator();
            if (event.getSource() == inoltra) {
                creator_controller.inoltra(email);
                creator_controller.setUpMail();
                if (stage != null)
                    stage.show();
            } else {
                AlertUtility.alertError("Non puoi rispondere a una mail da te inviata!");
            }
        } else if (!inboxTable.getSelectionModel().isEmpty()) {
            email = inboxTable.getSelectionModel().getSelectedItem();
            Stage stage = setCreator();
            if (event.getSource() == rispondi)
                creator_controller.rispondi(email);
            else if (event.getSource() == rispondi_tutti)
                creator_controller.rispondiTutti(email);
            else if (event.getSource() == inoltra)
                creator_controller.inoltra(email);
            creator_controller.setUpMail();
            if (stage != null)
                stage.show();
        }
        if (email == null) {
            AlertUtility.alertError("Non hai selezionato alcuna mail");
        }
    }

    @FXML
    private void cancellaMail(ActionEvent event) {
        try {
            if (!sentTable.getSelectionModel().isEmpty()) {
                Email selected = sentTable.getSelectionModel().getSelectedItem();
                server_remote.cancellaInviata(client_model.getAddress(), selected);
                sentTable.getItems().remove(selected);
            } else if (!inboxTable.getSelectionModel().isEmpty()) {
                Email selected = inboxTable.getSelectionModel().getSelectedItem();
                inboxTable.getItems().remove(selected);
                server_remote.cancellaRicevuta(client_model.getAddress(), selected);
            } else {
                AlertUtility.alertError("Non hai selezionato alcuna mail.");
            }
        } catch (RemoteException e) {
            AlertUtility.alertError("Non sei connesso al server.");
        }
    }

    private Stage setCreator() {
        Stage stage = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("MailCreatorView.fxml"));
            Parent root = loader.load();
            stage = new Stage();
            creator_controller = loader.getController();
            stage.setTitle("Nuova Email");
            stage.setScene(new Scene(root, 400, 450));
            creator_controller.setUpCreator(server_remote, client_model);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    class CheckMail extends Task {
        Email nuova = null;
        public Void call() {
            while (true) {
                try {
                    Thread.sleep(5000);
                    nuova = server_remote.mailNonLetta(client_model.getAddress());
                    if (nuova != null) {
                        Platform.runLater(() -> {
                            AlertUtility.alertInfo("È arrivata una nuova email.");
                        });
                        synchronized (client_model){
                            client_model.getInbox().add(nuova);
                        }
                        synchronized (server_remote) {
                            server_remote.resetNewMail(client_model.getAddress());
                        }
                        nuova = null;
                    }
                } catch (RemoteException e) {
                    Platform.runLater(() -> {
                        AlertUtility.alertError("Impossibile connettersi al server.");
                    });
                } catch (InterruptedException e) {
                    Platform.runLater(() -> {
                        AlertUtility.alertError("Malfunzionamento del thread.");
                        System.exit(-1);
                    });
                }
            }
        }
    }

    public void shutdown() {
        try{
            server_remote.removeMailbox(client_model.getAddress());
            server_remote.addLog(client_model.getAddress() + " si è disconnesso.");
        } catch (RemoteException e){
            AlertUtility.alertError("Impossibile disconnettersi dal server.");
        }
    }

}
