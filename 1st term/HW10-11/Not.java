package expression;

public class Not extends UnaryFunction implements CommonExpression {
    public Not(CommonExpression x) {
        arg = x;
    }

    public int calc(int x) {
        return ~x;
    }

    public double calc(double x) {
        return calc((int)x);
    }
}
