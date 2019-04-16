package expression;

public class High extends UnaryFunction {
    public High(TripleExpression x) {
        super(x);
    }

    public int calc(int x) {
        return Integer.highestOneBit(x);
    }
}
