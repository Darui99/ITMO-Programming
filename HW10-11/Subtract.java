package expression;

public class Subtract extends BinaryFunction implements CommonExpression {
    public Subtract(CommonExpression a1, CommonExpression a2) {
        arg1 = a1;
        arg2 = a2;
    }

    public int calc(int x, int y) {
        return x - y;
    }

    public double calc(double x, double y) {
        return x - y;
    }
}
