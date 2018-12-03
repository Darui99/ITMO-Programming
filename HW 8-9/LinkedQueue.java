package queue;

public class LinkedQueue extends AbstractQueue implements Queue {
    private class Node {
        private Node next;
        private Object val;

        private Node(Object nval) {
            val = nval;
            next = null;
        }

        private void setLink(Node to) {
            next = to;
        }

        private Object getVal() {
            return val;
        }

        private Node getNext() {
            return next;
        }
    }

    private Node head, tail;

    public LinkedQueue() {
        head = new Node(null);
        tail = new Node(null);

        head.setLink(null);
        tail.setLink(null);
        size = 0;
    }

    public void enqueue(Object x) {
        Node cur = new Node(x);
        if (head.getNext() == null || tail.getNext() == null) {
            head.setLink(cur);
            tail.setLink(cur);
        } else {
            tail.getNext().setLink(cur);
            tail.setLink(cur);
        }
        size++;
    }

    public Object element() {
        assert head.getNext() != null;
        return head.getNext().getVal();
    }

    protected void popFront() {
        head.setLink(head.getNext().getNext());
    }

    public LinkedQueue makeCopy() {
        LinkedQueue res = new LinkedQueue();
        res.head.setLink(head.getNext());
        res.tail.setLink(tail.getNext());
        res.size = size;
        return res;
    }

    public LinkedQueue makeEmpty() {
        return new LinkedQueue();
    }

    public void clear() {
        head = new Node(null);
        tail = new Node(null);

        head.setLink(null);
        tail.setLink(null);
        size = 0;
    }
}
