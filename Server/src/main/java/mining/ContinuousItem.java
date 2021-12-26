package mining;

import data.ContinuousAttribute;

public class ContinuousItem extends Item{
    public ContinuousItem(ContinuousAttribute attribute, Interval value){
        super(attribute, value);
    }
    //da rivedere se mettere public o package
    boolean checkItemCondition(Object value){
        return ((Interval)this.getValue()).checkValueInclusion((Float)value);
    }

    public String toString() {
        return this.getAttribute() + " in " + this.getValue();
    }
}
