package mining;

public class EmergingPatternException extends Exception{
    public EmergingPatternException(){
        super("Pattern non soddisfa il minimo grow rate");
    }
}
