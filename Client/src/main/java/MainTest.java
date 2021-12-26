import keyboardinput.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class MainTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        InetAddress addr;
        Socket socket;
        int port;

        if(args.length == 0) {
            addr = InetAddress.getByName("localhost");
            port = 8080;
            socket = new Socket(addr, port);

        }
        else {
            addr = InetAddress.getByName(args[0]);
            port = new Integer(args[1]);
            socket = new Socket(addr, port);
        }
        System.out.println("addr = " + addr + "\nport=" + port);
        System.out.println(socket);

        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        char risp = 's';
        do {
            System.out.println("\nScegli una opzione:");
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
                System.out.println("Inserire valore minimo grow rate (minGr>=1):");
                minGr = Keyboard.readFloat();
            } while (minGr < 1);


            System.out.println("Tabella target:");
            String targetName = Keyboard.readString();

            System.out.println("Tabella background:");
            String backgroundName = Keyboard.readString();

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

    }
}

