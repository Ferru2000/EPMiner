package mining;

import data.Attribute;

import java.io.Serializable;

abstract class Item implements Serializable {
    private Attribute attribute;
    private Object value;

    Item(Attribute attribute,Object value) {
        this.attribute = attribute;
        this.value = value;
    }
    public Attribute getAttribute() {
        return this.attribute;
    }

    public Object getValue() {
        return value;
    }
    //da rivedere se mettere public o package
    abstract boolean checkItemCondition(Object value);

    public String toString() {
        return attribute + "=" + value;
    }
}
