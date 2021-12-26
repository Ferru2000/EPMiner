package mining;

import data.Data;
import data.EmptySetException;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EmergingPatternMiner implements Iterable<EmergingPattern>, Serializable {
    //lista che contiene riferimenti a oggetti istanza della classe EmergingPattern che definiscono il pattern
    private List<EmergingPattern> epList = new LinkedList<EmergingPattern>();

    public EmergingPatternMiner(Data dataBackground, FrequentPatternMiner fpList, float minG) throws EmptySetException {
        /*if (dataBackground.getNumberOfAttributes() == 0) {
            throw new EmptySetException();
        }
        Puntatore pointer = fpList.getOutputFP().firstList();
        while (!fpList.getOutputFP().endList(pointer)) {
            FrequentPattern fp = (FrequentPattern) fpList.getOutputFP().readList(pointer);
            try {
                EmergingPattern ep = computeEmergingPattern(dataBackground, fp, minG);
                epList.add(ep);

            } catch (EmergingPatternException excEm) {
                excEm.printStackTrace();
            } finally {
                pointer = fpList.getOutputFP().succ(pointer);
            }
        }*/
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

    float computeGrowRate(Data dataBackground, FrequentPattern fp) {

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

    @Override
    public Iterator<EmergingPattern> iterator() {
        return epList.iterator();
    }

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
    public void salva(String nomeFile) throws FileNotFoundException, IOException {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));){
            out.writeObject(this);
        }
    }

    public static EmergingPatternMiner carica(String nomeFile) throws FileNotFoundException,IOException,ClassNotFoundException {
        EmergingPatternMiner epm = null;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));){
            epm = (EmergingPatternMiner) in.readObject();
            return epm;
        }
    }
}
