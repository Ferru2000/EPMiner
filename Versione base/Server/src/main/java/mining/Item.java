package mining;

import data.Attribute;

import java.io.Serializable;

/**
 * Questa Classe astratta gestisce un generico item in forma attributo-valore.
 */
abstract class Item implements Serializable {
    private Attribute attribute;
    private Object value;

    /**
     * Costruttore della classe che assegna i valori agli attributi.
     * @param attribute Attributo
     * @param value Valore dell'attributo
     */
    Item(Attribute attribute,Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Metodo che restituisce l'attributo dell'item.
     * @return Attributo dell'item
     */
    public Attribute getAttribute() {
        return this.attribute;
    }

    /**
     * Metodo che restituisce il valore dell'item.
     * @return Valore dell'item
     */
    public Object getValue() {
        return value;
    }

    /**
     * Metodo astratto che verifica l'appartenenza di value al dominio di Item.
     * @param value Valore di cui controllare l'appartenenza al dominio
     * @return valore booleano che indica l'appartenzenza di value al dominio
     */
    abstract boolean checkItemCondition(Object value);

    /**
     * Metodo che restituisce l'oggetto come stringa.
     * @return Stringa che rappresenta l'oggetto Item
     */
    public String toString() {
        return attribute + "=" + value;
    }
}
