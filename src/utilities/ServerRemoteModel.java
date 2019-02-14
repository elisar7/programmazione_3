package utilities;

import javafx.collections.ObservableList;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerRemoteModel extends Remote {
    void addLog(String event) throws RemoteException;

    boolean loadMailbox(String address) throws RemoteException;

    void cancellaInviata(String address, Email email) throws RemoteException;

    void cancellaRicevuta(String address, Email email) throws RemoteException;

    void inviaMail(Email email) throws RemoteException;
    boolean checkDestinatario(String address) throws RemoteException;
    ArrayList<Email> getSent(String address) throws RemoteException;
    ArrayList<Email> getInbox(String address) throws RemoteException;
    ObservableList<Log> getLogs() throws RemoteException;
    void resetNewMail(String address) throws RemoteException;
    void removeMailbox (String address) throws RemoteException;
    Email mailNonLetta(String address) throws RemoteException;
}
