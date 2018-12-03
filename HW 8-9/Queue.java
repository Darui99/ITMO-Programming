package queue;

public interface Queue extends Copiable {
    // Pre: x != null
    void enqueue (Object x);
    // Post: size' = size + 1

    // Pre: size > 0
    Object element();
    // Post: Queue didn't change

    // Pre: size > 0
    Object dequeue();
    // Post: size' = size - 1

    int size();
    // Post: Queue didn't change

    boolean isEmpty();
    // Post: Queue didn't change

    void clear();
    // Post: size' = 0, Queue is empty
}
