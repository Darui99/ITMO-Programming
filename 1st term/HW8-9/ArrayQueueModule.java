package queue;

public class ArrayQueueModule {
    private static int head = 0, tail = 0, size = 0, capasity = 1;
    private static Object[] arr = new Object[1];

    // Pre: size = capasity
    private static void rebuild() {
        Object[] newArr = new Object[capasity * 2];
        for (int i = head, step = 0; step < size; i = (i + 1) % capasity, step++)
            newArr[step] = arr[i];
        arr = newArr;
        head = 0;
        tail = capasity;
        capasity *= 2;
    }
    // Post: capasity' = capasity * 2, arr' = arr(head;size-1) + arr(0; tail), head' = 0, tail' = 0

    // Pre: x != null
    public static void enqueue(Object x) {
        arr[tail] = x;
        size++;
        if (capasity == size) {
            rebuild();
        } else {
            tail = (tail + 1) % capasity;
        }
    }
    // Post: size' = size + 1

    // Pre: x != null
    public static void push(Object x) {
        head = (head - 1 + capasity) % capasity;
        arr[head] = x;
        size++;
        if (capasity == size) {
            rebuild();
        }
    }
    // Post: size' = size + 1

    // Pre: size > 0
    public static Object peek() {
        return arr[(tail - 1 + capasity) % capasity];
    }

    // Pre: size > 0
    private static void popBack() {
        tail = (tail - 1 + capasity) % capasity;
        size--;
    }
    // Post: size' = size - 1

    // Pre: size > 0
    public static Object remove() {
        Object res = peek();
        popBack();
        return res;
    }
    // Post: size' = size - 1

    // Pre: size > 0
    public static Object element() {
        return arr[head];
    }

    // Pre: size > 0
    private static void popFront() {
        head = (head + 1) % capasity;
        size--;
    }
    // size' = size - 1

    // Pre: size > 0
    public static Object dequeue() {
        Object res = arr[head];
        popFront();
        return res;
    }
    // Post: size' = size - 1

    public static int size() {
        return size;
    }

    public static boolean isEmpty() {
        return (size == 0);
    }

    public static void clear() {
        arr = new Object[1];
        head = tail = 0;
        size = 0;
        capasity = 1;
    }
}
