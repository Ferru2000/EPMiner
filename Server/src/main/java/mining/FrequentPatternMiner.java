package mining;

import data.*;
import utility.EmptyQueueException;
import utility.Queue;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FrequentPatternMiner implements Iterable<FrequentPattern> , Serializable {
    private List<FrequentPattern> outputFP = new LinkedList<FrequentPattern>();


    /*public FrequentPatternMiner(Data data, float minSup) throws EmptySetException {
        if (data.getNumberOfAttributes() == 0) {
            throw new EmptySetException();
        }
        Queue<FrequentPattern> fpQueue = new Queue<FrequentPattern>();
        //La classe Data non implementa Iterable quindi non è possibile utilizzare gli iteratori
        for (int i = 0; i < data.getNumberOfAttributes(); i++) {
            Attribute currentAttribute = data.getAttribute(i);
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
        try {
            outputFP = expandFrequentPatterns(data, minSup, fpQueue, outputFP);
        } catch (EmptyQueueException excQueue) {
            excQueue.printStackTrace();
        }
        sort();
    }

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

    private FrequentPattern refineFrequentPattern(FrequentPattern FP, Item item) {
        FrequentPattern newFP = new FrequentPattern(FP);
        newFP.addItem(item);
        return newFP;
    }

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

    public List<FrequentPattern> getOutputFP() {
        return outputFP;
    }

    @Override
    public Iterator<FrequentPattern> iterator() {
        return outputFP.iterator();
    }

    private void sort() {
        //FrequentPattern temp = new FrequentPattern();

        /*boolean swap = false;
        while(!swap) {
            swap = false;
            for (int i = 1; i < getOutputFP().size() - 1; i++) {
                if (getOutputFP().get(i - 1).compareTo(getOutputFP().get(i)) > 0.0f) {
                    FrequentPattern temp = getOutputFP().get(i - 1);
                    getOutputFP().set(i - 1, getOutputFP().get(i));
                    getOutputFP().set(i, temp);
                    swap = true;
                }
            }
        }*/

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

    public void salva(String nomeFile) throws FileNotFoundException, IOException {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));){
            out.writeObject(this);
        }
    }

    public static FrequentPatternMiner carica(String nomeFile) throws FileNotFoundException,IOException,ClassNotFoundException {
        FrequentPatternMiner fpm = null;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));){
            fpm = (FrequentPatternMiner) in.readObject();
            return fpm;
        }
    }
}
	


