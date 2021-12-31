package mining;

import data.ContinuousAttribute;

/**
 * Questa classe gestisce un Item con coppia attributo-valore continui
 */
public class ContinuousItem extends Item{

    /**
     * Costruttore della classe in cui si richiama il costruttore della superclasse.
     * @param attribute Attributo dell'item
     * @param value Valore dell'item
     */
    ContinuousItem(ContinuousAttribute attribute, Interval value){
        super(attribute, value);
    }

    /**
     * Metodo che verifica se il parametro value rappresenti un numero reale incluso tra gli estremi dell’intervallo
     * associato all' item in oggetto
     * @param value Valore di cui controllare l'appartenenza all'intervallo
     * @return Booleano che indica se il valore appartiene o meno all'intervallo
     */
    boolean checkItemCondition(Object value){
        return ((Interval)this.getValue()).checkValueInclusion((Float)value);
    }

    /**
     * Metodo che restituisce una stringa in formato (nome attributo) in [inf, sup[.
     * @return Stringa che rappresenta l'oggetto ContinousItem
     */
    public String toString() {
        return this.getAttribute() + " in " + this.getValue();
    }
}
