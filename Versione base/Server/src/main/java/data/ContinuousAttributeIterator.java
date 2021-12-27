package data;

import java.util.Iterator;

/**
 * Questa classe gestisce un iteratore per ContinuousAttribute.
 */
public class ContinuousAttributeIterator implements Iterator<Float> {

	private float min;
	private float max;
	private int j=1;
	private int numValues;

	/**
	 * Costruttore che inizializza gli attributi di ContinuousAttribute.
	 * @param min Estremo inferiore dell'intervallo
	 * @param max Estremo superiore dell'intervallo
	 * @param numValues Numero di sotto-intervalli di uguali dimenioni in cui dividere l'intervallo
	 */
	ContinuousAttributeIterator(float min,float max,int numValues){
		this.min=min;
		this.max=max;
		this.numValues=numValues;
	}

	/**
	 * Metodo che verifica la presenza di un segmento successivo
	 * @return Booleano che indica se vi è un segmento successivo
	 */
	@Override
	public boolean hasNext() {
		return (j<=numValues);
	}

	/**
	 * Metodo che restituisce il primo valore del successivo segmento.
	 * @return Numero del segmento successivo
	 */
	public Float next() {
		j++;
		return min+((max-min)/numValues)*(j-1);
	}
}
