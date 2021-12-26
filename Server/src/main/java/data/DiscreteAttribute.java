package data;

public class DiscreteAttribute extends Attribute {
    String[] values;

    DiscreteAttribute(String name, int index, String[] values) {
        super(name, index);
        this.values = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }
    public int getNumberOfDistinctValues() {
        return values.length;
    }
    public String getValue(int index) {
        return values[index];
    }
    /*public String toString() {
        return
    }*/
}
