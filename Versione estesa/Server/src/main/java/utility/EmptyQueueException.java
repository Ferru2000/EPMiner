package utility;

/**
 * Questa classe gestisce l'eccezione di una coda vuota.
 */
public class EmptyQueueException extends Exception{
    public EmptyQueueException(){
        super("Coda vuota");
    }
}
