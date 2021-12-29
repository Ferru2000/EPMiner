package mining;

import data.*;
import utility.EmptyQueueException;
import utility.Queue;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Questa classe utilizza l'algoritmo Apriori per la scoperta di pattern frequenti.
 */
public class FrequentPatternMiner implements Iterable<FrequentPattern> , Serializable {
    private List<FrequentPattern> outputFP = new LinkedList<FrequentPattern>();

    /**
     * Costruttore che genera i pattern a partire dai pattern di lunghezza 1. Per espandere i pattern viene
     * richiamato il metodo expandFrequentPatterns.
     * @param data Insieme delle transizioni
     * @param minSup Valore di minimo supporto
     * @throws EmptySetException Se il dataset è vuoto
     */
    public FrequentPatternMiner(Data data, float minSup) throws EmptySetException {
        if (data.getNumberOfAttributes() == 0) {
            throw new EmptySetException();
        }
        Queue<FrequentPattern> fpQueue = new Queue<FrequentPattern>();
        //La classe Data non implementa Iterable quindi non è possibile utilizzare gli iteratori
        for (int i = 0; i < data.getNumberOfAttributes(); i++) {
            Attribute currentAttribute = data.getAttribute(i);
            if (currentAttribute instanceof DiscreteAttribute) {
                for (int j = 0; j < ((DiscreteAttribute) currentAttribute).getNumberOfDistinctValues(); j++) {
                    DiscreteItem item = new DiscreteItem(
                            (DiscreteAttribute) currentAttribute,
                            ((DiscreteAttribute) currentAttribute).getValue(j));
                    FrequentPattern fp = new FrequentPattern();
                    fp.addItem(item);
                    fp.setSupport(fp.computeSupport(data));
                    if (fp.getSupport() >= minSup) { // 1-FP CANDIDATE
                        fpQueue.enqueue(fp);
                        //System.out.println(fp);
                        outputFP.add(fp);
                    }

                }
            }
            else if (currentAttribute instanceof ContinuousAttribute) {

                Float cutPoint = 0.0f;
                Float min = 0.0f;
                //il problema è che istanzia sempre un nuovo iterator
                Iterator<Float> it = ((ContinuousAttribute) currentAttribute).iterator();
                while (it.hasNext()) {
                    cutPoint = it.next();

                    ContinuousItem item = new ContinuousItem(
                            (ContinuousAttribute) currentAttribute, new Interval(min , cutPoint));
                    FrequentPattern fp = new FrequentPattern();
                    fp.addItem(item);
                    fp.setSupport(fp.computeSupport(data));
                    if (fp.getSupport() >= minSup) { // 1-FP CANDIDATE
                        fpQueue.enqueue(fp);
                        //System.out.println(fp);
                        outputFP.add(fp);
                    }

                    min = +cutPoint;

                }
            }
        }

        try {
            outputFP = expandFrequentPatterns(data, minSup, fpQueue, outputFP);
        } catch (EmptyQueueException excQueue) {
            excQueue.printStackTrace();
        }
        sort();
    }

    /**
     * Metodo che crea un nuovo pattern a cui aggiunge l'oggetto FP e il parametro item.
     * @param FP Pattern da raffinare.
     * @param item Item da aggiungere ad FP.
     * @return Il nuovo pattern ottenuto tramite raffinamento
     */
    private FrequentPattern refineFrequentPattern(FrequentPattern FP, Item item) {
        FrequentPattern newFP = new FrequentPattern(FP);
        newFP.addItem(item);
        return newFP;
    }

    /**
     * Metodo che espande la lista di pattern frequenti.
     * @param data Insieme delle transizioni
     * @param minSup Valore di minimo supporto
     * @param fpQueue Coda di pattern da valutare e, nel caso, raffinare
     * @param outputFP Lista dei pattern frequenti già estratti
     * @return Lista contentente pattern frequenti di lunghezza {@literal >} 1
     * @throws EmptyQueueException Se la coda è vuota
     */
    private List<FrequentPattern> expandFrequentPatterns(Data data, float minSup, Queue<FrequentPattern> fpQueue, List<FrequentPattern> outputFP) throws EmptyQueueException {
        if (fpQueue.isEmpty()) {
            throw new EmptyQueueException();
        }

        while (!fpQueue.isEmpty()) {
            FrequentPattern fp = fpQueue.first(); //fp to be refined
            fpQueue.dequeue();
            for (int i = 0; i < data.getNumberOfAttributes(); i++) {
                boolean found = false;
                for (Item item : fp) {
                    //for (int j = 0; j < fp.getPatternLength(); j++) //the new item should involve an attribute different form attributes already involved into the items of fp
                    //if (fp.getItem(j).getAttribute().equals(data.getAttribute(i))) {
                    if (item.getAttribute().equals(data.getAttribute(i))) {
                        found = true;
                        break;
                    }
                }
                if (!found)//data.getAttribute(i) is not involve into an item of fp
                {
                    if (data.getAttribute(i) instanceof DiscreteAttribute) {

                        for (int j = 0; j < ((DiscreteAttribute) data.getAttribute(i)).getNumberOfDistinctValues(); j++) {
                            DiscreteItem item = new DiscreteItem(
                                    (DiscreteAttribute) data.getAttribute(i),
                                    ((DiscreteAttribute) (data.getAttribute(i))).getValue(j)
                            );
                            FrequentPattern newFP = refineFrequentPattern(fp, item); //generate refinement
                            newFP.setSupport(newFP.computeSupport(data));
                            if (newFP.getSupport() >= minSup) {
                                fpQueue.enqueue(newFP);
                                //System.out.println(newFP);
                                outputFP.add(newFP);
                            }
                        }
                    } else if (data.getAttribute(i) instanceof ContinuousAttribute) {

                        float cutPoint = 0.0f;
                        float min = 0.0f;

                        Iterator<Float> it = ((ContinuousAttribute) data.getAttribute(i)).iterator();
                        while (it.hasNext()) {
                            cutPoint = it.next();

                            ContinuousItem item = new ContinuousItem(
                                    (ContinuousAttribute) data.getAttribute(i),
                                    new Interval(min, cutPoint));
                            FrequentPattern newFP = refineFrequentPattern(fp, item); //generate refinement
                            newFP.setSupport(newFP.computeSupport(data));
                            if (newFP.getSupport() >= minSup) {
                                fpQueue.enqueue(newFP);
                                //System.out.println(newFP);
                                outputFP.add(newFP);
                            }

                            min =+ cutPoint;

                        }
                    }

                }
            }
        }
        return outputFP;
    }

    /**
     * Metodo che scandisce la lista outputFP e concatena gli elementi di tale lista in una stringa.
     * @return Stringa che rappresenta outputFP
     */
    public String toString() {
        //Puntatore Pointer = outputFP.firstList();
        String completePattern = "Frequent patterns: \n";
        int i = 0;
        FrequentPattern fp;
        //while(!outputFP.endList(Pointer)) {
        for (FrequentPattern item : outputFP) {
            fp = item;
            completePattern = completePattern.concat(Integer.toString(i + 1) + ":" + fp.toString() + "\n");
            //Pointer = outputFP.succ(Pointer);
            i++;
        }

        return completePattern;
    }

    /**
     * Metodo che restituisce la lista dei pattern frequenti.
     * @return Lista di FrequentPattern
     */
    public List<FrequentPattern> getOutputFP() {
        return outputFP;
    }

    /**
     * Metodo che restituisce un iterator per la lista FrequentPattern.
     * @return Iteratore per la lista outputFP
     */
    @Override
    public Iterator<FrequentPattern> iterator() {
        return outputFP.iterator();
    }

    /**
     * Metodo che ordina la lista outputFP
     */
    private void sort() {
        for (int i = 0; i < getOutputFP().size(); i++) {
            for (int j = 1; j < getOutputFP().size() - i; j++) {
                if (getOutputFP().get(j - 1).compareTo(getOutputFP().get(j)) > 0) {
                    FrequentPattern temp = getOutputFP().get(j - 1);
                    getOutputFP().set(j - 1, getOutputFP().get(j));
                    getOutputFP().set(j, temp);
                }
            }
        }

    }

    /**
     * Metodo che salva su un file i pattern frequenti.
     * @param nomeFile Stringa che indica il nome del file
     * @throws IOException Se vi e' una eccezione di input/output
     */
    public void salva(String nomeFile) throws IOException {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));){
            out.writeObject(this);
        }
    }

    /**
     * Metodo che carica i pattern frequenti da un file.
     * @param nomeFile Stringa che indica il nome del file da cui caricare i dati
     * @return FrequentPatternMiner caricato dal file
     * @throws IOException Se vi e' una eccezione di input/output
     * @throws ClassNotFoundException Se la classe non viene trovata
     */
    public static FrequentPatternMiner carica(String nomeFile) throws IOException,ClassNotFoundException {
        FrequentPatternMiner fpm = null;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));){
            fpm = (FrequentPatternMiner) in.readObject();
            return fpm;
        }
    }
}
	


