package data;

public class EmptySetException extends Exception{
    public EmptySetException() {
        super("Insieme di transazioni vuoto");
    }
}
