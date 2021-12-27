package data;

/**
 * Questa classe gestisce un'eccezione di tipo EmptySetException.
 */
public class EmptySetException extends Exception{
    public EmptySetException() {
        super("Insieme di transazioni vuoto");
    }
}
