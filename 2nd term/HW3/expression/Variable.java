package expression;

public class Variable<T> implements TripleExpression<T> {
    private String name;

    public Variable(String x) {
        name = x;
    }

    public T evaluate(T x, T y, T z) {
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
