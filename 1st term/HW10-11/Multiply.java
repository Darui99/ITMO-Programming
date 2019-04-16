package expression;

public class Multiply extends BinaryFunction implements CommonExpression {
    public Multiply(CommonExpression a1, CommonExpression a2) {
        arg1 = a1;
        arg2 = a2;
    }

    public int calc(int x, int y) {
        return x * y;
    }

    public double calc(double x, double y) {
        return x * y;
    }
}
