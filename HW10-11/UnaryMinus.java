package expression;

public class UnaryMinus extends UnaryFunction implements CommonExpression {
    public UnaryMinus(CommonExpression x) {
        arg = x;
    }

    public int calc(int x) {
        return -x;
    }

    public double calc(double x) {
        return calc((int)x);
    }
}
