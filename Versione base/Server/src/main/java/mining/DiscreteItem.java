package mining;

import data.DiscreteAttribute;

/**
 * Questa classe gestisce un Item con coppia attributo-valore discreti
 */
public class DiscreteItem extends Item {

    /**
     * Costruttore della classe in cui si richiama il costruttore della superclasse.
     * @param attribute Attributo dell'item
     * @param value Valore dell'item
     */
    DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute,value);
    }

    /**
     * Metodo che verifica se il valore dell'item sia uguale a quello passato come parametro.
     * @param value Valore di cui controllare l'appartenenza al dominio
     * @return Booleano che indica se i valori confrontati sono uguali o meno
     */
    boolean checkItemCondition(Object value) {
        return this.getValue().equals(value);
    }
}
