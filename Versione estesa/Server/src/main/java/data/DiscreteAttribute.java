package data;

/**
 * Questa classe rappresenta un attributo discreto.
 */
public class DiscreteAttribute extends Attribute {
    String[] values;

    /**
     * Costruttore di classe che inizializza un attributo discreto.
     * @param name Nome dell'attributo
     * @param index Indice dell'attributo
     * @param values Valori dell'attributo
     */
    DiscreteAttribute(String name, int index, String[] values) {
        super(name, index);
        this.values = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    /**
     * Questo metodo restituisce il numero di valori discreti nel dominio dell'attributo.
     * @return Numero di valori discreti
     */
    public int getNumberOfDistinctValues() {
        return values.length;
    }
    public String getValue(int index) {
        return values[index];
    }

}
