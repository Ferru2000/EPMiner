package data;

import java.io.Serializable;

public abstract class Attribute implements Serializable {
    protected String name;
    protected final int index;

    Attribute(String name,int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public String toString() {
        return name;
    }
}
