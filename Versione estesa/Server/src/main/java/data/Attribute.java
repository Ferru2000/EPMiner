package data;

import java.io.Serializable;

/**
 * Questa classe astratta gestisce un attributo generico.
 */
public abstract class Attribute implements Serializable {
    protected String name;
    protected final int index;

    /**
     * Costruttore che inizializza gli attributi della classe.
     * @param name Nome dell'attributo
     * @param index Indice dell'attributo
     */
    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Metodo che restituisce il nome dell'attributo.
     * @return Nome dell'attributo
     */
    String getName() {
        return name;
    }

    /**
     * Metodo che restituisce l'indice dell'attributo.
     * @return Indice dell'attributo
     */
    public int getIndex() {
        return index;
    }

    /**
     * Metodo che restituisce una stringa contenente il nome dell'attributo
     * @return Stringa contenente il nome dell'attributo
     */
    public String toString() {
        return name;
    }
}
