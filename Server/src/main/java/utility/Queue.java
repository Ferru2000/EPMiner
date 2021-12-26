package utility;

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


    public boolean isEmpty() {
        return this.begin == null;
    }

    public void enqueue(T e) {
        if (this.isEmpty())
            this.begin = this.end = new Record(e);
        else {
            this.end.next = new Record(e);
            this.end = this.end.next;
        }
    }


    public T first() {
        return this.begin.elem;
    }

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