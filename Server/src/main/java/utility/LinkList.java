package utility;

public class LinkList {


    private Puntatore inizioLista = null;


    public boolean isEmpty() {
        return inizioLista == null;
    }

    public Puntatore firstList() {
        return null;

    }
    public Puntatore getInizioLista() { return inizioLista;}

    public boolean endList(Puntatore p) {
        if (isEmpty()) return true;
        if (p == firstList())
            return inizioLista == null; // verifica che la lista sia vuota
        else
            return ((Puntatore) p).link.successivo == null; //verifica che elemento successivo a quello in posizione p sia nullo
    }


    public Object readList(Puntatore p) {
        if (isEmpty())
            throw new IndexOutOfBoundsException("Lista vuota");
        if (p == firstList())
            return inizioLista.link.elemento;
        else
            return ((Puntatore) p).link.successivo.link.elemento;


    }

    public void add(Object e) { //aggiunge in testa
        Puntatore temp;

        if (!isEmpty()) {
            temp = inizioLista;
            inizioLista = new Puntatore(new Cella(e));
            inizioLista.link.successivo = temp;
        } else {
            // se la lista � vuota
            inizioLista = new Puntatore(new Cella(e));
        }

    }


    public Puntatore succ(Puntatore p) {
        if (endList(p))
            throw new IndexOutOfBoundsException(
                    "Posizione fine lista non valida");
        if (isEmpty())
            throw new IndexOutOfBoundsException("Lista vuota");
        if (p == firstList())
            return inizioLista;
        else if (p == inizioLista)
            return inizioLista.link.successivo;
        else
            return ((Puntatore) p).link.successivo;
    }


}
