package mining;

import java.io.Serializable;

public class Interval implements Serializable {
    private float inf;
    private float sup;

    Interval(float inf, float sup) {
        this.inf = inf;
        this.sup = sup;
    }

    float getInf() {
        return inf;
    }

    void setInf(float inf) {
        this.inf = inf;
    }

    float getSup() {
        return sup;
    }

    void setSup(float sup) {
        this.sup = sup;
    }

    boolean checkValueInclusion(float value) {
        //<= in qunanto se no 30.3 non viene considerato e poi il support risulta più basso
        // return (value >= inf && value < sup);
        return (value >= inf && value <= sup);
    }

    public String toString() {
        return "[" + inf + "," + sup + "[";
    }


}
