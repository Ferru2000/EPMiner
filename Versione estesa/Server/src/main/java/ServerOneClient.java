import data.Data;
import data.EmptySetException;
import database.DatabaseConnectionException;
import database.NoValueException;
import mining.EmergingPatternMiner;
import mining.FrequentPatternMiner;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Questa classe gestisce la connessione con un nuovo client.
 */
public class ServerOneClient extends Thread {

    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    /**
     * Costruttore che inizializza la socket e gli stream di input e output.
     * @param s Descrittore della socket
     * @throws IOException  Se vi e' una eccezione di input/output
     */
    ServerOneClient(Socket s) throws IOException {
        socket = s;
        in = new ObjectInputStream(s.getInputStream());
        out = new ObjectOutputStream(s.getOutputStream());
        start();
    }

    /**
     * Metodo che esegue le richieste del client.
     */
    @Override
    public void run() {
        char risposta;
        String targetName = "";
        String backgroundName = "";
        Float minsup;
        Float minGr;
        boolean exit = false;

        String directoryName = "archivio";
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }
        String archivePath = directory.getAbsolutePath() + "\\";

        while (!exit) {
            try {
                System.out.println("in attesa dell'operazione da eseguire..");

                int numOperazione = (int) in.readObject();
                minsup = (Float) in.readObject();
                minGr = (Float) in.readObject();
                targetName = (String) in.readObject();
                backgroundName = (String) in.readObject();

                FrequentPatternMiner fpMiner = null;
                EmergingPatternMiner epMiner = null;

                if (numOperazione == 1) {
                    try {
                        Data dataTarget = new Data(targetName);
                        Data dataBackground = new Data(backgroundName);

                        try {
                            fpMiner = new FrequentPatternMiner(dataTarget, minsup);

                            try {
                                fpMiner.salva(archivePath + "FP_" + targetName + "_" + minsup + ".dat");
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("I/O exception");
                            }

                        } catch (EmptySetException e) {
                            e.printStackTrace();
                            System.out.println("Empty set exception");
                        }

                        try {
                            epMiner = new EmergingPatternMiner(dataBackground, fpMiner, minGr);

                            try {
                                epMiner.salva(archivePath + "EP_" + backgroundName + minsup + "_minGr" + minGr + ".dat");
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("I/O exception");
                            }

                        } catch (EmptySetException e) {
                            e.printStackTrace();
                            System.out.println("Empty set exception");
                        }

                        if (fpMiner != null & epMiner != null) {
                            out.writeObject(fpMiner.toString());
                            out.writeObject(epMiner.toString());
                        }
                    } catch (NoValueException e) {
                        e.printStackTrace();
                        System.out.println("Dati non trovati");

                        out.writeObject("Errore");
                        out.writeObject("Errore");
                    } catch (DatabaseConnectionException e) {
                        e.printStackTrace();
                        System.out.println("Errore nella connessione al databese");

                        out.writeObject("Errore");
                        out.writeObject("Errore");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Tabella non trovata");

                        out.writeObject("Errore");
                        out.writeObject("Errore");
                    }
                } else {
                    try {
                        fpMiner = FrequentPatternMiner.carica(archivePath + "FP_" + targetName + "_" + minsup + ".dat");
                        out.writeObject(fpMiner.toString());

                        epMiner = EmergingPatternMiner.carica(archivePath + "EP_" + backgroundName + minsup + "_minGr" + minGr + ".dat");
                        out.writeObject(epMiner.toString());
                    } catch (FileNotFoundException e) {
                        System.out.println("File non trovato.");
                        out.writeObject("Errore");
                        out.writeObject("Tabella inserita non trovata");
                    }
                }

            } catch (IOException e) {
                exit = true;
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                exit = true;
                e.printStackTrace();
            }
        }
    }
}
