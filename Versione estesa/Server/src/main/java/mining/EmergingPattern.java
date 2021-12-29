package mining;

import java.io.Serializable;

/**
 * Questa classe rappresenta un pattern emergente
 */
public class EmergingPattern extends FrequentPattern implements Serializable {
    private float growrate;

    /**
     * Costruttore della classe EmergingPattern. Vengono inizializzate le variabili d'istanza con i parametri passati.
     * @param fp FrequentPattern
     * @param growrate Grow rate
     */
    EmergingPattern(FrequentPattern fp, float growrate) {
        super(fp);
        this.growrate = growrate;
    }

    /**
     * Metodo che restituisce il grow rate del pattern emergente.
     * @return Grow rate del pattern
     */
    public float getGrowrate() {
        return this.growrate;
    }

    /**
     * Metodo che crea una stringa concatenando il pattern e il grow rate.
     * @return Stringa che rappresenta il pattern emergente
     */
    public String toString() {
        String value;
        value = super.toString() + "[" + growrate + "]";
        return value;
    }
}
