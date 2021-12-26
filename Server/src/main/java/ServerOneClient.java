import data.Data;
import data.EmptySetException;
import database.DatabaseConnectionException;
import database.NoValueException;
import mining.EmergingPatternMiner;
import mining.FrequentPatternMiner;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class ServerOneClient extends Thread {

    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    ServerOneClient(Socket s) throws IOException {
        socket = s;
        in = new ObjectInputStream(s.getInputStream());
        out = new ObjectOutputStream(s.getOutputStream());
        start();
    }

    public void run() {

        char risposta;

        String targetName = "";
        String backgroundName = "";
        Float minsup;
        Float minGr;
        boolean exit=false;

        String directoryName = "archivio";
        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }
        String archivePath = directory.getAbsolutePath() + "\\";

        try {
            while (!exit) {

                Data dataTarget = null;
                Data dataBackground = null;

                System.out.println("in attesa dell'operazione da eseguire..");
                int numOperazione = (int) in.readObject();

                minsup = (Float) in.readObject();
                minGr = (Float) in.readObject();

                targetName = (String) in.readObject();
                try {
                    dataTarget = new Data(targetName);
                } catch (SQLException e) {
                    out.writeObject("Errore");
                } catch (DatabaseConnectionException | NoValueException e) {
                    out.writeObject(e.getMessage());
                }

                backgroundName = (String) in.readObject();
                try {
                    dataBackground = new Data(backgroundName);
                } catch (SQLException e) {
                    out.writeObject("Errore");
                } catch (DatabaseConnectionException | NoValueException e) {
                    out.writeObject(e.getMessage());
                }

                FrequentPatternMiner fpMiner;
                EmergingPatternMiner epMiner;
                if (numOperazione == 1) {
                    try {
                        fpMiner = new FrequentPatternMiner(dataTarget, minsup);
                        try {
                            fpMiner.salva(archivePath + "FP_" + targetName + "_" + minsup + ".dat");
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            out.writeObject(e1.getMessage());
                        }

                        //out.writeObject(fpMiner);
                        out.writeObject(fpMiner.toString());

                        try {
                            epMiner = new EmergingPatternMiner(dataBackground, fpMiner, minGr);
                            try {
                                epMiner.salva(archivePath + "EP_" + backgroundName + minsup + "_minGr" + minGr + ".dat");
                            } catch (IOException e1) {
                                // TODO Auto-generated catch block
                                out.writeObject(e1.getMessage());
                            }
                            //out.writeObject(epMiner);
                            out.writeObject(epMiner.toString());

                        } catch (EmptySetException e) {
                            out.writeObject(e.getMessage());

                        }

                    } catch (EmptySetException e) {
                        e.printStackTrace();
                    }


                } else if (numOperazione == 2) {
                    try {
                        fpMiner = FrequentPatternMiner.carica(archivePath + "FP_" + targetName + "_" + minsup + ".dat");
                        out.writeObject(fpMiner.toString());

                        epMiner = EmergingPatternMiner.carica(archivePath + "EP_" + backgroundName + minsup + "_minGr" + minGr + ".dat");
                        out.writeObject(epMiner.toString());

                    } catch (ClassNotFoundException
                            | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                risposta = (char) in.readObject();
                if (risposta == 'n') {
                        exit = true;
                }
                //out.writeObject(true);
            }
            //System.out.println("Closing..");
        } catch (
                IOException e) {
            System.err.println("Errore");
        } catch (
                ClassNotFoundException e) {
            System.err.println("ClassNotFound Exception");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }
}
