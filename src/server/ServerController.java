package server;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import utilities.Log;
import utilities.ServerRemoteModel;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerController {

    ServerRemoteModel server_remote;

    @FXML
    private TableView<Log> logTable;
    @FXML
    private TableColumn<Log, String> eventColumn;
    @FXML
    private TableColumn<Log, String> timestampColumn;

    @FXML
    public void initialize() {
        setUpRemote();
        setUpView();
    }

    private void setUpView() {
        try {
            logTable.setItems(server_remote.getLogs());
            eventColumn.setCellValueFactory(cellData ->
                    cellData.getValue().getMessaggioProperty());
            timestampColumn.setCellValueFactory(cellData ->
                    cellData.getValue().getTimestampProperty());
        } catch (RemoteException e) {
            System.out.println("Errore di connessione");
            System.exit(-1);
        }

    }

    private void setUpRemote() {
        try {
            server_remote = new ServerModel();
            lanciaRMIRegistry();
            Naming.rebind("rmi://127.0.0.1:2000/server",
                    server_remote);
        } catch (RemoteException e) {
            System.out.println("Errore di connessione");
            System.exit(-1);
        } catch (MalformedURLException e) {
            System.out.println("Errore nell'URL del server remoto");
            System.exit(-1);
        }
    }

    private void lanciaRMIRegistry() throws RemoteException {
        try {
            LocateRegistry.createRegistry(2000);
            server_remote.addLog("È stato creato il registro RMI");
        } catch (RemoteException e) {
            server_remote.addLog("Il registro RMI esiste già");
        }
    }

}
