package expression;

public abstract class UnaryFunction implements CommonExpression {
    protected CommonExpression arg;

    public UnaryFunction() {
        arg = null;
    }

    public abstract int calc(int x);
    public abstract double calc(double x);

    public int evaluate(int x) {
        return calc(arg.evaluate(x));
    }

    public double evaluate(double x) {
        return calc(arg.evaluate(x));
    }

    public int evaluate(int x, int y, int z) {
        return calc(arg.evaluate(x, y, z));
    }
}
