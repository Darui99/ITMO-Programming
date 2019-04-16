package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue implements Queue {
    private int head, tail, capasity;
    private Object[] arr;

    public ArrayQueue() {
        arr = new Object[1];
        head = tail = 0;
        size = 0;
        capasity = 1;
    }

    // Pre: size = capasity
    private void rebuild() {
        Object[] newArr = new Object[capasity * 2];
        for (int i = head, step = 0; step < size; i = (i + 1) % capasity, step++)
            newArr[step] = arr[i];
        arr = newArr;
        head = 0;
        tail = capasity;
        capasity *= 2;
    }
    // Post: capasity' = capasity * 2, arr' = arr(head;size-1) + arr(0; tail), head' = 0, tail' = 0

    public void enqueue(Object x) {
        arr[tail] = x;
        size++;
        if (capasity == size) {
            rebuild();
        } else {
            tail = (tail + 1) % capasity;
        }
    }

    // Pre: size > 0
    public Object peek() {
        return arr[(tail - 1 + capasity) % capasity];
    }

    // Pre: size > 0
    private void popBack() {
        tail = (tail - 1 + capasity) % capasity;
        size--;
    }

    public Object element() {
        return arr[head];
    }

    // Pre: size > 0
    protected void popFront() {
        head = (head + 1) % capasity;
    }
    // size' = size - 1

    public ArrayQueue makeCopy() {
        final ArrayQueue res = new ArrayQueue();
        res.size = size;
        res.head = head;
        res.tail = tail;
        res.capasity = capasity;
        res.arr = Arrays.copyOf(arr, capasity);
        return res;
    }

    public ArrayQueue makeEmpty() {
        return new ArrayQueue();
    }

    public void clear() {
        arr = new Object[1];
        head = tail = 0;
        size = 0;
        capasity = 1;
    }
}
