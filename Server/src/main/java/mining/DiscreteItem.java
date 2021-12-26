package mining;

import data.DiscreteAttribute;

public class DiscreteItem extends Item {
    DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute,value);
    }

    boolean checkItemCondition(Object value) {
        return this.getValue().equals(value);
    }
}
