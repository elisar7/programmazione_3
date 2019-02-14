package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.Email;
import utilities.ServerRemoteModel;

import java.rmi.RemoteException;

public class ClientModel {

    private String address;
    private ServerRemoteModel server_remote;
    private ObservableList<Email> sent;
    private ObservableList<Email> inbox;

    public ClientModel() {
        sent = FXCollections.observableArrayList();
        inbox = FXCollections.observableArrayList();
    }

    public void setServerModel(ServerRemoteModel server_remote) {
        this.setServer_remote(server_remote);
    }

    public boolean setUpMailbox()
            throws RemoteException {
        if (server_remote.loadMailbox(getAddress())) {
            sent.addAll(server_remote.getSent(getAddress()));
            inbox.addAll(server_remote.getInbox(getAddress()));
            return true;
        } else return false;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ServerRemoteModel getServer_remote() {
        return server_remote;
    }

    public void setServer_remote(ServerRemoteModel server_remote) {

        this.server_remote = server_remote;
    }

    public ObservableList<Email> getSent() {
        return sent;
    }

    public ObservableList<Email> getInbox() {
        return inbox;
    }

}
