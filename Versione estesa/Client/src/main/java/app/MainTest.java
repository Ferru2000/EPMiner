package app;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class MainTest {

    public boolean executeQuery(String address, String port) throws IOException, ClassNotFoundException {

        InetAddress addr = InetAddress.getByName(address);
        System.out.println("addr = " + address + "\nport=" + port);
        Socket socket = new Socket(addr, new Integer(port));
        System.out.println(socket);


        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        // stream con richieste del client


        // get RadioButton choice
        int opzione;
        if(MainScene.getMainScene().getRadioOption_1().isSelected()) {
            opzione = 1;
            System.out.println("opzione 1");
        }
        else {
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

            try{
                fpMiner = (String) (in.readObject());
                epMiner = (String) (in.readObject());
            }catch(EOFException e) {
                return false;
            }


            MainScene.getMainScene().getFrequentPatternText().setText(fpMiner);
            System.out.println(fpMiner);
            MainScene.getMainScene().getEmergingPatternText().setText(epMiner);
            System.out.println(epMiner);

            if(fpMiner.equals("Errore") || epMiner.equals("Errore")) {
                esito = false;
            }
            else {
                MainScene.getMainScene().getFrequentPatternText().setText(fpMiner);
                System.out.println(fpMiner);
                MainScene.getMainScene().getEmergingPatternText().setText(epMiner);
                System.out.println(epMiner);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            socket.close();
            out.close();
            in.close();
        }

        return esito;
/*
        char risp = 's';
        do {
            System.out.println("Scegli una opzione:");
            int opzione;
            do {
                System.out.println("1:Nuova scoperta");
                System.out.println("2:Risultati in archivio");
                opzione = Keyboard.readInt();
            } while (opzione != 1 && opzione != 2);

            float minsup = 0f;
            float minGr = 0f;
            do {
                System.out.println("Inserire valore minimo supporto (minsup>0 e minsup<=1):");
                minsup = Keyboard.readFloat();
            } while (minsup <= 0 || minsup > 1);

            do {
                System.out.println("Inserire valore minimo grow rate (minGr>0):");
                minGr = Keyboard.readFloat();
            } while (minGr <= 0);


            System.out.println("Tabella target:");
            String targetName = Keyboard.readString();

            System.out.println("Tabella background:");
            String backgroundName = Keyboard.readString();

            String nameFile = targetName + "_" + backgroundName;

            try {
                out.writeObject(opzione);
                out.writeObject(minsup);
                out.writeObject(minGr);
                out.writeObject(targetName);
                out.writeObject(backgroundName);

                String fpMiner = (String) (in.readObject());
                System.out.println(fpMiner);

                String epMiner = (String) (in.readObject());
                System.out.println(epMiner);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("Vuoi ripetere?(s/n)");
            risp = Keyboard.readChar();
            out.writeObject(risp);
        } while (risp != 'n');
    */
    }
}

