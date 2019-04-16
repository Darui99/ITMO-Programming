package expression;

public class Min extends BinaryFunction {
    public Min(TripleExpression a1, TripleExpression a2) {
        super(a1, a2);
    }

    public int calc(int x, int y) {
        if (x < y)
            return x;
        else
            return y;
    }
}
