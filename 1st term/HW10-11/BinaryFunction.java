package expression;

public abstract class BinaryFunction implements CommonExpression {
    protected CommonExpression arg1, arg2;

    public BinaryFunction() {
        arg1 = null;
        arg2 = null;
    }

    public abstract int calc(int x, int y);
    public abstract double calc(double x, double y);

    public int evaluate(int x) {
        return calc(arg1.evaluate(x), arg2.evaluate(x));
    }

    public double evaluate(double x) {
        return calc(arg1.evaluate(x), arg2.evaluate(x));
    }

    public int evaluate(int x, int y, int z) {
        return calc(arg1.evaluate(x, y, z), arg2.evaluate(x, y, z));
    }
}
