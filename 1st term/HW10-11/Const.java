package expression;

public class Const implements CommonExpression {
    private Number value;

    public Const(int x) {
        value = x;
    }

    public Const(double x) {
        value = x;
    }

    public int evaluate(int x) {
        return value.intValue();
    }

    public double evaluate(double x) {
        return value.doubleValue();
    }

    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }
}
