package mining;

import data.ContinuousAttribute;
import data.Data;
import data.DiscreteAttribute;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Questa classe gestisce un pattern frequente.
 */
public class FrequentPattern implements Iterable<Item>, Comparable<FrequentPattern>, Serializable {

    private LinkedList<Item> fp;
    private float support;

    /**
     * Costruttore della classe.
     */
    FrequentPattern() {
        fp = new LinkedList<>();
    }

    /**
     * Costruttore di copia.
     * @param FP FrequentPattern da copiare nella variabile d'istanza fp
     */
    FrequentPattern(FrequentPattern FP) {
        //int length = FP.getPatternLength();
        fp = new LinkedList<>();
        Iterator<Item> it = FP.iterator();
        for (int i = 0; it.hasNext(); i++) {
            //fp.add(FP.getItem(i));
            addItem(FP.getItem(i));
            it.next();
        }
        support = FP.getSupport();
    }

    /**
     * Metodo che aggiunge un nuovo item al pattern.
     * @param item Item da aggiungere al pattern
     */
    void addItem(Item item) {
        fp.add(item);
    }

    /**
     * Metodo che restituisce l'item in posizione index nella lista fp.
     * @param index Posizione dell'item da restituire
     * @return Item in posizione index
     */
    public Item getItem(int index) {
        return fp.get(index);
    }

    /**
     * Metodo che restituisce il support del FrequentPattern.
     * @return support
     */
    public float getSupport() {
        return support;
    }

    /**
     * Metodo che restituisce la lunghezza del pattern.
     * @return lunghezza del pattern
     */
    public int getPatternLength() {
        return fp.size();
    }

    /**
     * Metodo che assegna alla variabile d'istanza pattern il valore passato come parametro.
     * @param support Supporto da assegnare all'attributo della classe support
     */
    public void setSupport(float support) {
        this.support = support;
    }

    /**
     * Metodo che scandisce fp e concatena in una stringa gli item contenuti in esso.
     * @return Stringa che rappresenta il FrequentPattern
     */
    public String toString() {
        String value = "";
        for (int i = 0; i < fp.size() - 1; i++)
            value += "(" + fp.get(i) + ")" + " AND ";
        if (fp.size() > 0) {
            value += "(" + fp.get(fp.size() - 1) + ")";
            value += "[" + support + "]";
        }
        return value;
    }

    /**
     * Metodo che calcola il support dell'oggetto FrequentPattern rispetto al dataset data.
     * @param data Dataset con cui calcolare il supporto
     * @return Supporto del pattern rispetto al dataset
     */
    float computeSupport(Data data) {
        int suppCount = 0;
        // indice esempio
        for (int i = 0; i < data.getNumberOfExamples(); i++) {
            //indice item
            boolean isSupporting = true;
            //for (int j = 0; j < this.getPatternLength(); j++)
            for (Item it : this) {
                //DiscreteItem
                if(it instanceof DiscreteItem) {
                    DiscreteItem item = (DiscreteItem) it;
                    DiscreteAttribute attribute = (DiscreteAttribute) item.getAttribute();
                    Object valueInExample = data.getAttributeValue(i, attribute.getIndex());
                    if (!item.checkItemCondition(valueInExample)) {
                        isSupporting = false;
                        break; //the ith example does not satisfy fp
                    }
                }
                else  if(it instanceof ContinuousItem){
                    ContinuousItem item = (ContinuousItem) it;
                    ContinuousAttribute attribute = (ContinuousAttribute) item.getAttribute();
                    Object valueInExample = data.getAttributeValue(i, attribute.getIndex());
                    //andare a veder chexckitemcondition
                    if (!item.checkItemCondition(valueInExample)) {
                        isSupporting = false;
                        break; //the ith example does not satisfy fp
                    }
                }

            }
            if (isSupporting)
                suppCount++;
        }
        return ((float) suppCount) / (data.getNumberOfExamples());

    }

    /**
     * Metodo che restituisce un iteratore di FrequentPattern.
     * @return Iteratore
     */
    @Override
    public Iterator<Item> iterator() {
        return fp.iterator();
    }

    /**
     * Metodo che confronta il supporto del FrequentPattern corrente con quello passato come parametro.
     * @param o FrequentPattern di cui confrontare il supporto con quello dell'oggetto this
     * @return 1 se this.support {@literal >} o.support, 0 se sono uguali, -1 se this.support {@literal <} o.support
     */
    @Override
    public int compareTo(FrequentPattern o) {
        float diff = (this.support - o.getSupport());
        int res = 0;
        if (diff > 0.0f)
            res = 1;
        else if (diff < 0.0f)
            res = -1;
        return res;
    }
}
