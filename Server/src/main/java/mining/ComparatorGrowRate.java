package mining;

import java.util.Comparator;

public class ComparatorGrowRate implements Comparator<EmergingPattern> {
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
