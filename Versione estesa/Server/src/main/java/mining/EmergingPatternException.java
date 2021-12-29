package mining;

/**
 * Questa classe gestisce l'eccezione di un pattern che non soddisfa il minimo grow rate
 */
public class EmergingPatternException extends Exception{
    public EmergingPatternException(){
        super("Pattern non soddisfa il minimo grow rate");
    }
}
