package utility;

public class EmptyQueueException extends Exception{
    public EmptyQueueException(){
        super("Coda vuota");
    }
}
