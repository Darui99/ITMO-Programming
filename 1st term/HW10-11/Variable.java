package expression;

public class Variable implements CommonExpression {
    private String name;

    public Variable(String x) {
        name = x;
    }

    public int evaluate(int x) {
        return x;
    }

    public double evaluate(double x) {
        return x;
    }

    public int evaluate(int x, int y, int z) {
        if (name.equals("x")) {
            return x;
        } else {
            if (name.equals("y")) {
                return y;
            } else {
                return z;
            }
        }
    }
}
