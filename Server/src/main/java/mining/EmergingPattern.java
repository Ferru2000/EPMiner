package mining;

import java.io.Serializable;

public class EmergingPattern extends FrequentPattern implements Serializable {
    private float growrate;

    EmergingPattern(FrequentPattern fp, float growrate) {
        super(fp);
        this.growrate = growrate;
    }

    public float getGrowrate() {
        return this.growrate;
    }

    public String toString() {
        String value = new String();
        value = super.toString() + "[" + growrate + "]";
        return value;
    }
}
