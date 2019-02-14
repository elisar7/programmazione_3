package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import utilities.AlertUtility;
import utilities.Email;
import utilities.ServerRemoteModel;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientMailCreatorController {

    private ServerRemoteModel server_remote;
    private ClientModel client_model;
    private Email templateMail;

    //MailCreator View
    @FXML
    private TextArea testoField;
    @FXML
    private TextField mittenteField;
    @FXML
    private TextField destinatarioField;
    @FXML
    private TextField oggettoField;
    @FXML
    private Button inviaMail;

    public void setUpCreator(ServerRemoteModel server_remote, ClientModel client_model) {
        this.server_remote = server_remote;
        this.client_model = client_model;
        templateMail = new Email(client_model.getAddress());
        mittenteField.setText(templateMail.getMittente());
    }

    public void rispondi(Email template) {
        templateMail.getDestinatari().add(template.getMittente());
    }

    public void rispondiTutti(Email template) {
        templateMail.getDestinatari().add(template.getMittente());
        for (int i = 0; i < template.getDestinatari().size(); i++) {
            if (!template.getDestinatari().get(i).equals(client_model.getAddress()))
                templateMail.getDestinatari().add((template.getDestinatari().get(i)));
        }
    }

    public void inoltra(Email template) {
        testoField.setText(template.getTesto());
    }

    public void setUpMail() {
        mittenteField.setText(templateMail.getMittente());
        destinatarioField.setText(templateMail.getDestinatari().toString());
    }

    @FXML
    public void invia(ActionEvent event) {
        try {
            if (!oggettoField.getText().isEmpty()) {
                templateMail.setOggetto(oggettoField.getText());
            }
            if (!testoField.getText().isEmpty()) {
                templateMail.setTesto(testoField.getText());
            }
            templateMail.setTesto(testoField.getText());
            if (destinatarioField.getText().equals("[]")) {
                AlertUtility.alertError("Devi specificare almeno un destinatario!");
            } else if (destinatarioField.getText().equals(client_model.getAddress())) {
                AlertUtility.alertError("Non puoi mandarti una mail da solo!");
            } else {
                String dest = destinatarioField.getText();
                Scanner scanner = new Scanner(dest.
                        substring(1, dest.length() - 1)).
                        useDelimiter(", ");
                Boolean flag = true;
                if (templateMail.getDestinatari().isEmpty()) {
                    while (scanner.hasNext()) {
                        String destinatario = scanner.next();
                        if (server_remote.checkDestinatario(destinatario)) {
                            if (!destinatario.equals(client_model.getAddress())) {
                                templateMail.getDestinatari().add(destinatario);
                            } else {
                                AlertUtility.alertError("Non puoi mandarti una mail da solo!");
                                flag = false;
                            }
                        } else {
                            AlertUtility.alertError("Il destinatario " + destinatario + " non esiste");
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    server_remote.inviaMail(templateMail);
                    client_model.getSent().add(templateMail);
                    System.out.println(templateMail.toString());;
                    AlertUtility.alertInfo("La tua mail è stata inviata con successo.");
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                }
            }
        } catch (RemoteException e) {
            AlertUtility.alertError("Non sei connesso al server.");
        }
    }
}
