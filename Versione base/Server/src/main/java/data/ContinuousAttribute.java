package data;

import java.util.Iterator;

/**
 * Questa classe gestisce un attributo continuo.
 */
public class ContinuousAttribute extends Attribute implements Iterable<Float> {
    private float max;
    private float min;

    /**
     * Costruttore che inizializza gli attributi della classe.
     * @param name Nome dell'attributo
     * @param index Indice dell'attributo
     * @param min Estremo inferiore dell'intervallo
     * @param max Estremo inferiore dell'intervallo
     */
    ContinuousAttribute(String name, int index, float min, float max) {
        super(name, index);
        this.max = max;
        this.min = min;
    }

    /**
     * Metodo che restituisce l'attributo min.
     * @return Estremo inferiore dell'intervallo
     */
    public float getMin() {
        return min;
    }

    /**
     * Metodo che restituisce l'attributo max.
     * @return Estremo superiore dell'intervallo
     */
    public float getMax() {
        return max;
    }

    /**
     * Metodo che restituisce un iteratore per ContinuousAttribute.
     * @return Iteratore
     */
    public Iterator<Float> iterator() {
        return new ContinuousAttributeIterator(min, max, 5);
    }


}
