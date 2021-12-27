package utility;

/**
 * Questa classe gestisce una generica coda.
 * @param <T> Tipo deglie elementi della coda
 */
public class Queue<T> {

    private Record begin = null;

    private Record end = null;

    private class Record {

        T elem;

        Record next;

        Record(T e) {
            this.elem = e;
            this.next = null;
        }
    }


    /**
     * Metodo che verifica se una coda è vuota o no.
     * @return Valore booleano che indica se la coda è vuota o no
     */
    public boolean isEmpty() {
        return this.begin == null;
    }

    /**
     * Metodo che inserisce un elemento alla fine della coda.
     * @param e Elemento da inserire nella coda
     */
    public void enqueue(T e) {
        if (this.isEmpty())
            this.begin = this.end = new Record(e);
        else {
            this.end.next = new Record(e);
            this.end = this.end.next;
        }
    }


    /**
     * Metodo che restituisce il primo elemento della coda,
     * @return Primo elemento della coda
     */
    public T first() {
        return this.begin.elem;
    }

    /**
     * Metodo che elimina il primo elemento della coda.
     * @throws EmptyQueueException se la coda è vuota
     */
    public void dequeue() throws EmptyQueueException{
        if (this.begin == this.end) {
            if (this.begin == null) {
                System.out.println("The queue is empty!");
                throw new EmptyQueueException();
            }
            else
                this.begin = this.end = null;
        } else {
            begin = begin.next;
        }

    }

}