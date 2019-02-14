package server;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.Email;
import utilities.Log;
import utilities.Mailbox;
import utilities.ServerRemoteModel;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerModel extends UnicastRemoteObject implements ServerRemoteModel {

    private ArrayList<String> indirizzi;
    private ArrayList<Mailbox> mailboxes;
    private ObservableList<Log> logs;

    public ServerModel() throws RemoteException {
        this.indirizzi = new ArrayList<>();
        this.indirizzi.add("utente1@mail.com");
        this.indirizzi.add("utente2@mail.com");
        this.indirizzi.add("utente3@mail.com");
        this.mailboxes = new ArrayList<>();
        this.logs = FXCollections.observableArrayList();
    }

    public boolean loadMailbox(String address) {
        if (getMailboxForAddress(address) == null) {
            this.mailboxes.add(new Mailbox(address));
            addLog("La casella postale di " + address + " è stata caricata");
            return true;
        } else {
            addLog("Un altro utente ha cercato di connettersi all'account di " + address);
            return false;
        }
    }

    public void addLog(String event) {
        logs.add(new Log(event));
    }

    public void inviaMail(Email email) {
        scriviMail("./resources/" + email.getMittente() + "-sent.txt", email);
        getMailboxForAddress(email.getMittente()).incrCounter();
        getMailboxForAddress(email.getMittente()).getSent().add(email);
        for (int i = 0; i < email.getDestinatari().size(); i++) {
            String destinatario = email.getDestinatari().get(i);
            consegnaMail(email, destinatario);
        }
        addLog(email.getMittente() + " ha inviato una mail");
    }

    public void consegnaMail(Email email, String destinatario) {
        if (getMailboxForAddress(destinatario) != null) {
            getMailboxForAddress(destinatario).setNuovaEmail(email);
            getMailboxForAddress(destinatario).incrCounter();
            getMailboxForAddress(destinatario).getInbox().add(email);
            addLog(destinatario + " ha ricevuto una mail");
        }
        scriviMail("./resources/" + destinatario + "-inbox.txt", email);
    }

    public void scriviMail(String path, Email email) {
        try {
            FileWriter fw = new FileWriter(path, true);
            fw.write(email.toString() + "\n");
            fw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Non ho trovato il file");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("ERRORE IO");
            System.exit(-1);
        }
    }

    public void resetNewMail(String address) {
        if (getMailboxForAddress(address) != null) {
            getMailboxForAddress(address).setNuovaEmail(null);
        }
    }


    public void cancellaInviata(String address, Email email) {
        Mailbox tmp = getMailboxForAddress(address);
        if (tmp != null) {
            tmp.cancellaInviata(email);
            addLog(address + " ha cancellato una mail inviata");
        } else {
            addLog("Errore imprevisto");
            System.exit(-1);
        }
    }

    public void cancellaRicevuta(String address, Email email) {
        Mailbox tmp = getMailboxForAddress(address);
        if (tmp != null) {
            tmp.cancellaRicevuta(email);
            addLog(address + " ha cancellato una mail ricevuta");
        } else {
            addLog("Errore imprevisto");
            System.exit(-1);
        }
    }

    public Email mailNonLetta(String address) {
        Mailbox tmp = getMailboxForAddress(address);
        if (tmp != null) {
            return tmp.getNuovaEmail();
        } else {
            addLog("Errore imprevisto");
            System.exit(-1);
        }
        return null;
    }

    public void removeMailbox (String address){
        for (int i = 0; i < mailboxes.size(); i++) {
            if (mailboxes.get(i).getAddress().equals(address))
                mailboxes.remove(i);
        }
    }

    private Mailbox getMailboxForAddress(String address) {
        for (int i = 0; i < mailboxes.size(); i++) {
            if (mailboxes.get(i).getAddress().equals(address))
                return mailboxes.get(i);
        }
        return null;
    }

    public boolean checkDestinatario(String address) {
        for (int i = 0; i < indirizzi.size(); i++) {
            if (indirizzi.get(i).equals(address))
                return true;
        }
        return false;
    }

    public ArrayList<Email> getInbox(String address) {
        return getMailboxForAddress(address).getInbox();
    }

    public ArrayList<Email> getSent(String address) {
        return getMailboxForAddress(address).getSent();
    }

    public ObservableList<Log> getLogs() {
        return this.logs;
    }

    public ArrayList<String> getIndirizzi() {
        return this.indirizzi;
    }

}
