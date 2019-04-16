package expression;

public class Low extends UnaryFunction {
    public Low(TripleExpression x) {
        super(x);
    }

    public int calc(int x) {
        return Integer.lowestOneBit(x);
    }
}
