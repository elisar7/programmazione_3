package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Mailbox implements Serializable {

    private String address;
    private int counter;
    private ArrayList<Email> sent;
    private ArrayList<Email> inbox;
    private Email nuovaEmail;
    private Scanner scanner1;
    private Scanner scanner2;
    private Scanner scanner3;

    public Mailbox(String address) {
        this.counter = 1;
        this.address = address;
        this.setSent(new ArrayList<>());
        this.setInbox(new ArrayList<>());
        this.setNuovaEmail(null);
        this.setScanner1(null);
        this.setScanner2(null);
        this.setScanner3(null);
        readMail("./resources/" + address + "-inbox.txt", getInbox());
        readMail("./resources/" + address + "-sent.txt", getSent());
    }

    public void cancellaInviata(Email email) {
        for (int i = 0; i < getSent().size(); i++) {
            if (getSent().get(i).getID() == email.getID()) {
                getSent().remove(i);
            }
        }
        writeMail("./resources/" + address + "-sent.txt", sent, false);
    }


    public void cancellaRicevuta(Email email) {
        for (int i = 0; i < getInbox().size(); i++) {
            if (getInbox().get(i).getID() == email.getID()) {
                getInbox().remove(i);
            }
        }
        writeMail("./resources/" + address + "-inbox.txt", inbox, false);
    }

    public void writeMail(String path, ArrayList<Email> email, Boolean append) {
        try {
            FileWriter fw = new FileWriter(path, append);
            for (int i = 0; i < email.size(); i++) {
                fw.write(email.get(i).toString() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMail(String filepath, ArrayList<Email> email) {
        try {
            setScanner1(new Scanner(new File(filepath)));
            while (getScanner1().hasNextLine()) {
                ArrayList<String> destinatari = new ArrayList<>();
                setScanner2(new Scanner(getScanner1().nextLine()).useDelimiter(
                        "\\s*%\\s*"));
                if (getScanner2().hasNext()) {
                    String mittente = getScanner2().next();
                    if (getScanner2().hasNext()) {
                        setScanner3(new Scanner(getScanner2().next()).useDelimiter("\\s*,\\s*"));
                        while (getScanner3().hasNext()) {
                            String destinatario = getScanner3().next();
                            destinatari.add(destinatario);
                        }
                        System.out.println(destinatari);
                        if (getScanner2().hasNext()) {
                            String oggetto = getScanner2().next();
                            if (getScanner2().hasNext()) {
                                String testo = getScanner2().next();
                                if (getScanner2().hasNext()) {
                                    String timestamp = getScanner2().next();
                                    email.add(new Email(getCounter(), mittente, destinatari,
                                            oggetto, testo, timestamp));
                                    counter = counter + 1;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        String result = "Address: " + getAddress() + "\n";
        result += "Messaggi inviati: \n";
        for (int i = 0; i < getSent().size(); i++) {
            result += getSent().get(i).toString() + "\n";
        }
        result += "Messaggi ricevuti: \n";
        for (int i = 0; i < getInbox().size(); i++) {
            result += getInbox().get(i).toString() + "\n";
        }
        return result;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCounter() {
        return counter;
    }

    public void incrCounter() {
        this.counter = this.counter + 1;
    }

    public ArrayList<Email> getSent() {
        return sent;
    }

    public void setSent(ArrayList<Email> sent) {
        this.sent = sent;
    }

    public ArrayList<Email> getInbox() {
        return inbox;
    }

    public void setInbox(ArrayList<Email> inbox) {
        this.inbox = inbox;
    }

    public Email getNuovaEmail() {
        return nuovaEmail;
    }

    public void setNuovaEmail(Email nuovaEmail) {
        this.nuovaEmail = nuovaEmail;
    }

    public Scanner getScanner1() {
        return scanner1;
    }

    public void setScanner1(Scanner scanner1) {
        this.scanner1 = scanner1;
    }

    public Scanner getScanner2() {
        return scanner2;
    }

    public void setScanner2(Scanner scanner2) {
        this.scanner2 = scanner2;
    }

    public Scanner getScanner3() {
        return scanner3;
    }

    public void setScanner3(Scanner scanner3) {
        this.scanner3 = scanner3;
    }
}
