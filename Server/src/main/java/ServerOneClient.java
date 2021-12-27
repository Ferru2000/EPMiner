import data.Data;
import data.EmptySetException;
import database.DatabaseConnectionException;
import database.NoValueException;
import mining.EmergingPatternMiner;
import mining.FrequentPatternMiner;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
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

                System.out.println(numOperazione);
                System.out.println(minsup);
                System.out.println(minGr);
                System.out.println(targetName);
                System.out.println(backgroundName);

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
    // da cancellare
    public void run2() {

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

        try {
            while (!exit) {
                int numOperazione = (int) in.readObject();
                minsup = (Float) in.readObject();
                minGr = (Float) in.readObject();
                targetName = (String) in.readObject();
                backgroundName = (String) in.readObject();

                System.out.println("in attesa dell'operazione da eseguire..");

                Data dataTarget = null;
                Data dataBackground = null;

                try {
                    dataTarget = new Data(targetName);
                } catch (SQLException e) {
                    System.out.println("eccezione qui");
                    out.writeObject("Errore");
                } catch (DatabaseConnectionException | NoValueException e) {
                    System.out.println("eccezione qui2");
                    out.writeObject(e.getMessage());
                }

                try {
                    dataBackground = new Data(backgroundName);
                } catch (SQLException e) {
                    out.writeObject("Errore");
                } catch (DatabaseConnectionException | NoValueException e) {
                    out.writeObject(e.getMessage());
                }

                System.out.println("prova");
                FrequentPatternMiner fpMiner;
                EmergingPatternMiner epMiner;
                if (numOperazione == 1) {
                    try {
                        System.out.println("fpminer");
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

                    } catch (ClassNotFoundException | IOException e) {
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
        } catch (IOException e) {
            System.err.println("Errore");
        } catch (ClassNotFoundException e) {
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
