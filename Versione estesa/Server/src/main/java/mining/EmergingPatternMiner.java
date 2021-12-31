package mining;

import data.Data;
import data.EmptySetException;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Questa classe gestisce la scoperta dei pattern emergente.
 */
public class EmergingPatternMiner implements Iterable<EmergingPattern>, Serializable {
    //lista che contiene riferimenti a oggetti istanza della classe EmergingPattern che definiscono il pattern
    private List<EmergingPattern> epList = new LinkedList<EmergingPattern>();

    /**
     * Costruttore che scandisce i frequent pattern in fpList, calcola il grow rate per ognuno di questi e
     * viene aggiunto ad epList se il valore ottenuto e' maggiore di minG.
     * @param dataBackground Dataset di background rispetto a cui calcolare il grow rate
     * @param fpList Lista dei frequent pattern
     * @param minG Valore minimo di supporto
     * @throws EmptySetException Se il dataset e' vuoto
     */
    public EmergingPatternMiner(Data dataBackground, FrequentPatternMiner fpList, float minG) throws EmptySetException {
        if (dataBackground.getNumberOfAttributes() == 0) {
            throw new EmptySetException();
        }
        int i = 0;

        Iterator<FrequentPattern> it = fpList.getOutputFP().iterator();
        //while (i<fpList.getOutputFP().size())
        while (it.hasNext()) {
            FrequentPattern fp = fpList.getOutputFP().get(i);
            try {
                EmergingPattern ep = computeEmergingPattern(dataBackground, fp, minG);
                epList.add(ep);

            } catch (EmergingPatternException excEm) {
                //excEm.printStackTrace();
            } finally {
                it.next();
                i++;
            }
        }
        sort();
    }

    /**
     * Metodo che calcola il supporto di fp rispetto al dataset target.
     * @param dataBackground Dataset di background contenente le transizioni
     * @param fp FrequentPattern di cui calcolare il grow rate
     * @return growrate di fp
     */
    private float computeGrowRate(Data dataBackground, FrequentPattern fp) {

        return fp.getSupport() / fp.computeSupport(dataBackground);
    }


    private EmergingPattern computeEmergingPattern(Data dataBackground, FrequentPattern fp, float minGR) throws EmergingPatternException {
        float growrate = computeGrowRate(dataBackground, fp);
        if (growrate >= minGR) {
            return new EmergingPattern(fp, growrate);
        } else {
            throw new EmergingPatternException();
        }
    }

    /**
     * Metodo che scandisce la lista epList e concatena gli elementi di tale lista in una stringa.
     * @return Stringa che rappresenta epList
     */
    public String toString() {
        //Puntatore Pointer = epList.firstList();
        String completeEP = "Emerging patterns \n";
        int i = 0;
        EmergingPattern ep;
        //while (!epList.endList(Pointer)) {
        for (EmergingPattern item : epList) {
            ep = item;

            completeEP += Integer.toString(i + 1) + ":" + ep.toString() + "\n";
            i++;

        }
        return completeEP;
    }

    /**
     * Metodo che restituisce un iterator per la lista EmergingPattern.
     * @return Iteratore
     */
    @Override
    public Iterator<EmergingPattern> iterator() {
        return epList.iterator();
    }

    /**
     * Metodo che ordina il vettore epList
     */
    private void sort(){
        ComparatorGrowRate comparing = new ComparatorGrowRate();
        for (int i = 0; i < epList.size(); i++) {
            for (int j = 1; j < epList.size() - i; j++) {
                if(comparing.compare(epList.get(j-1),epList.get(j))>0) {
                    EmergingPattern temp = epList.get(j-1);
                    epList.set(j-1,epList.get(j));
                    epList.set(j,temp);
                }
            }
        }
    }

    /**
     * Metodo che salva su un file i pattern emergenti.
     * @param nomeFile Stringa che indica il nome del file
     * @throws IOException Se vi e' una eccezione di input/output
     */
    public void salva(String nomeFile) throws IOException {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));){
            out.writeObject(this);
        }
    }

    /**
     * Metodo che carica i pattern emergenti da un file.
     * @param nomeFile Stringa che indica il nome del file da cui caricare i dati
     * @return EmergingPatternMiner caricato dal file
     * @throws IOException Se vi e' una eccezione di input/output
     * @throws ClassNotFoundException Se la classe non viene trovata
     */
    public static EmergingPatternMiner carica(String nomeFile) throws IOException,ClassNotFoundException {
        EmergingPatternMiner epm = null;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));){
            epm = (EmergingPatternMiner) in.readObject();
            return epm;
        }
    }
}
