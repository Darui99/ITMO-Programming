package queue;

public class ArrayQueueADT {
    private int head, tail, size, capasity;
    private Object[] arr;

    public ArrayQueueADT() {
        arr = new Object[1];
        head = tail = 0;
        size = 0;
        capasity = 1;
    }

    // Pre: size = capasity
    private static void rebuild(ArrayQueueADT q) {
        Object[] newArr = new Object[q.capasity * 2];
        for (int i = q.head, step = 0; step < q.size; i = (i + 1) % q.capasity, step++)
            newArr[step] = q.arr[i];
        q.arr = newArr;
        q.head = 0;
        q.tail = q.capasity;
        q.capasity *= 2;
    }
    // Post: capasity' = capasity * 2, arr' = arr(head;size-1) + arr(0; tail), head' = 0, tail' = 0

    // Pre: x != null
    public static void enqueue(ArrayQueueADT q, Object x) {
        q.arr[q.tail] = x;
        q.size++;
        if (q.capasity == q.size) {
            rebuild(q);
        } else {
            q.tail = (q.tail + 1) % q.capasity;
        }
    }
    // Post: size' = size + 1

    // Pre: x != null
    public static void push(ArrayQueueADT q, Object x) {
        q.head = (q.head - 1 + q.capasity) % q.capasity;
        q.arr[q.head] = x;
        q.size++;
        if (q.capasity == q.size) {
            rebuild(q);
        }
    }
    // Post: size' = size + 1

    // Pre: size > 0
    public static Object peek(ArrayQueueADT q) {
        return q.arr[(q.tail - 1 + q.capasity) % q.capasity];
    }

    // Pre: size > 0
    private static void popBack(ArrayQueueADT q) {
        q.tail = (q.tail - 1 + q.capasity) % q.capasity;
        q.size--;
    }
    // Post: size' = size - 1

    // Pre: size > 0
    public static Object remove(ArrayQueueADT q) {
        Object res = peek(q);
        popBack(q);
        return res;
    }
    // Post: size' = size - 1

    // Pre: size > 0
    public static Object element(ArrayQueueADT q) {
        return q.arr[q.head];
    }

    // Pre: size > 0
    private static void popFront(ArrayQueueADT q) {
        q.head = (q.head + 1) % q.capasity;
        q.size--;
    }
    // size' = size - 1

    // Pre: size > 0
    public static Object dequeue(ArrayQueueADT q) {
        Object res = q.arr[q.head];
        popFront(q);
        return res;
    }
    // Post: size' = size - 1

    public static int size(ArrayQueueADT q) {
        return q.size;
    }

    public static boolean isEmpty(ArrayQueueADT q) {
        return (q.size == 0);
    }

    public static void clear(ArrayQueueADT q) {
        q.arr = new Object[1];
        q.head = q.tail = 0;
        q.size = 0;
        q.capasity = 1;
    }
}
