package data;

import java.util.Iterator;

public class ContinuousAttribute extends Attribute implements Iterable<Float> {
    private float max;
    private float min;

    ContinuousAttribute(String name, int index, float min, float max) {
        super(name, index);
        this.max = max;
        this.min = min;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public Iterator<Float> iterator() {
        return new ContinuousAttributeIterator(min, max, 5);
    }


}
