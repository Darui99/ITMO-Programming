package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size;

    public abstract Object element();

    protected abstract void popFront();

    public Object dequeue() {
        assert size > 0;
        Object res = element();
        popFront();
        size--;
        return res;
    }

    public int size() {
        return size;
    }

    public abstract Queue makeCopy();

    public abstract Queue makeEmpty();

    public Queue filter(Predicate<Object> predicate) {
        Queue res = makeEmpty(), copy = makeCopy();

        for (int i = 0; i < size; i++) {
            Object cur = copy.dequeue();
            if (predicate.test(cur)) {
                res.enqueue(cur);
            }
        }
        return res;
    }
    // Post: size(res) <= size, for each res's element: predicate.test(element) = true

    public Queue map(Function<Object, Object> function) {
        Queue res = makeEmpty(), copy = makeCopy();

        for (int i = 0; i < size; i++) {
            res.enqueue(function.apply(copy.dequeue()));
        }
        return res;
    }
    // Post: for each element: element' = function(element)

    public Object[] toArray() {
        Object[] res = new Object[size];
        Queue copy = makeCopy();
        int ptr = 0;
        while (!copy.isEmpty()) {
            res[ptr++] = copy.dequeue();
        }
        return res;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public abstract void clear();
}
