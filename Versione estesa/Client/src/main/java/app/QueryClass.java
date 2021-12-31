package app;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Questa classe gestisce l'esecuzione della richiesta da parte del client.
 */
public class QueryClass {

    private Socket socket = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Metodo che inizializza la connessione con il server.
     * @param address Indirizzo del server
     * @param port Porta del server
     * @throws IOException Eccezione IOException
     */
    void initializeConnection(String address, String port) throws IOException {
        if (socket == null) {
            InetAddress addr = null;

            try {
                addr = InetAddress.getByName(address);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            socket = new Socket(addr, new Integer(port));

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Apertura connessione");
        }
    }

    /**
     * Metodo che chiude la connessione con il server.
     */
    void closeConnection() {
        if(socket != null) {
            try {
                socket.close();
                System.out.println("Chiusura connessione");
                out.close();
                in.close();

                // Dato che non è possibile aprire di nuovo una socke chiusa, setto a null
                // il socket in modo tale da crearne uno nuvo alla successiva richiesta
                socket = null;
                out = null;
                in = null;
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
    }

    /**
     * Metodo che prende i valori compilati nei form dell'interfaccia e li manda al server
     * per effettuare la richiesta.
     * @return Booleano che indica se la richiesta è andata a buon fine o meno
     * @throws IOException Eccezione IOException
     * @throws ClassNotFoundException Eccezione ClassNotFoundException
     */
    public boolean executeQuery() throws IOException, ClassNotFoundException {

        // get RadioButton choice
        int opzione;
        if (MainScene.getMainScene().getRadioOption_1().isSelected()) {
            opzione = 1;
            System.out.println("opzione 1");
        } else {
            opzione = 2;
            System.out.println("opzione 2");
        }

        // get minsup and mingrow values
        float minsup = Float.parseFloat(MainScene.getMainScene().getMinSupTextField().getText());
        float minGr = Float.parseFloat(MainScene.getMainScene().getMinGrowTextField().getText());

        System.out.println("minsup : " + minsup + "   mingrow : " + minGr);

        // get tables
        String targetName = MainScene.getMainScene().getTargetTextField().getText();
        String backgroundName = MainScene.getMainScene().getBackgroundTextField().getText();

        System.out.println("target: " + targetName + "   background: " + backgroundName);
        String nameFile = targetName + "_" + backgroundName;

        boolean esito = true;
        try {
            out.writeObject(opzione);
            out.writeObject(minsup);
            out.writeObject(minGr);
            out.writeObject(targetName);
            out.writeObject(backgroundName);

            String fpMiner = "";
            String epMiner = "";

            try {
                fpMiner = (String) (in.readObject());
                epMiner = (String) (in.readObject());
            } catch (EOFException e) {
                return false;
            }

            if (fpMiner.equals("Errore") || epMiner.equals("Errore")) {
                esito = false;
            } else {
                MainScene.getMainScene().getFrequentPatternText().setText(fpMiner);
                MainScene.getMainScene().getEmergingPatternText().setText(epMiner);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return esito;
    }
}

