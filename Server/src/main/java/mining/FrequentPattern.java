package mining;

import data.ContinuousAttribute;
import data.Data;
import data.DiscreteAttribute;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;


public class FrequentPattern implements Iterable<Item>, Comparable<FrequentPattern>, Serializable {

    /**
     * @uml.property name="fp"
     * @uml.associationEnd multiplicity="(0 -1)"
     */
    private LinkedList<Item> fp;
    /**
     * @uml.property name="support"
     */
    private float support;


    FrequentPattern() {
        fp = new LinkedList<>();
    }

    // costrutore per copia
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

    //aggiunge un nuovo item al pattern
    void addItem(Item item) {
        //int length = fp.size();
        //Item temp[] = new Item[length + 1];
        //System.arraycopy(fp, 0, temp, 0, length);
        //temp[length] = item;
        fp.add(item);


    }

    public Item getItem(int index) {
        return fp.get(index);
    }

    public float getSupport() {
        return support;
    }

    public int getPatternLength() {
        return fp.size();
    }

    public void setSupport(float support) {
        this.support = support;
    }

    public String toString() {
       /* String value = "";
        Iterator<Item> it = fp.iterator();
        while (it.hasNext()) {
            value += "(" + it + ")" + " AND ";
            it.next();
        }
        if (fp.size() > 0) {
            value += "(" + fp.get(fp.size() - 1) + ")";
            value += "[" + support + "]";
        }*/
        String value = "";
        for (int i = 0; i < fp.size() - 1; i++)
            value += "(" + fp.get(i) + ")" + " AND ";
        if (fp.size() > 0) {
            value += "(" + fp.get(fp.size() - 1) + ")";
            value += "[" + support + "]";
        }
        return value;
    }

    // Aggiorna il supporto
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
                /*//Extract the example value
                Object valueInExample = data.getAttributeValue(i, attribute.getIndex());
                if (!item.checkItemCondition(valueInExample)) {
                    isSupporting = false;
                    break; //the ith example does not satisfy fp
                }

                 */

            }
            if (isSupporting)
                suppCount++;
        }
        return ((float) suppCount) / (data.getNumberOfExamples());

    }

    @Override
    public Iterator<Item> iterator() {
        return fp.iterator();
    }

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
