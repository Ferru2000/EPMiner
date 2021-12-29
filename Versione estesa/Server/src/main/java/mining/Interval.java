package mining;

import java.io.Serializable;

/**
 * Questa classe modella un intervallo numerico continuo.
 */
public class Interval implements Serializable {
    private float inf;
    private float sup;

    /**
     * Costruttore della classe che assegna i valori alle variabili d'istanza.
     * @param inf Estremo inferiore dell'intervallo
     * @param sup Estremo superiore dell'intervallo
     */
    Interval(float inf, float sup) {
        this.inf = inf;
        this.sup = sup;
    }

    /**
     * Metodo che restituisce l'estremo inferiore dell'intervallo.
     * @return Estremo inferiore dell'intervallo
     */
    float getInf() {
        return inf;
    }

    /**
     * Metodo che setta l'attributo inf.
     * @param inf Valore da assegnare alla variabile d'istanza inf
     */
    void setInf(float inf) {
        this.inf = inf;
    }

    /**
     * Metodo che restituisce l'estremo superiore dell'intervallo.
     * @return Estremo superiore dell'intervallo
     */
    float getSup() {
        return sup;
    }

    /**
     * Metodo che setta l'attributo sup.
     * @param sup Valore da assegnare alla variabile d'istanza sup
     */
    void setSup(float sup) {
        this.sup = sup;
    }

    /**
     * Metodo che restituisce vero se il valore passato come paramentro è incluso nell'intervallo chiuso, falso altrimenti.
     * @param value Valore di cui verificare l'appartenenza nell'intervallo
     * @return booleano che indica se il valore è compreso nell'intervallo o meno
     */
    boolean checkValueInclusion(float value) {
        //<= in qunanto se no 30.3 non viene considerato e poi il support risulta più basso
        // return (value >= inf && value < sup);
        return (value >= inf && value <= sup);
    }

    /**
     * Metodo che rappresenta l'oggetto Interval in una stringa contenente l'intervallo specificato.
     * @return Stringa che rappresenta l'intervallo
     */
    public String toString() {
        return "[" + inf + "," + sup + "[";
    }


}
