package mining;

import java.util.Comparator;

/**
 * Questa classe funge da comparatore per EmergingPattern.
 */
public class ComparatorGrowRate implements Comparator<EmergingPattern> {

    /**
     * Metodo che confronta i growrate tra due EmergingPattern.
     * @param o1 Grow rate del primo pattern emergente
     * @param o2 Grow rate del secondo pattern emergente
     * @return intero: 1 se GrowRate di o1 maggiore, -1 se GrowRate di o2 maggiore, 0 altrimenti.
     */
    @Override
    public int compare(EmergingPattern o1, EmergingPattern o2) {
        float diff = (o1.getGrowrate()-o2.getGrowrate());
        int res = 0;

        if(diff>0.0f)
            res = 1;
        else if(diff<0.0f)
            res = -1;

        return res;
    }
}
