package expression;

import calculators.Calculator;
import expression.exceptions.EvaluateException;

public class Mod<T> extends BinaryFunction<T> {
    public Mod(TripleExpression<T> a1, TripleExpression<T> a2, Calculator<T> c) {
        super(a1, a2, c);
    }

    public T calc(T x, T y) throws EvaluateException {
        return calculator.mod(x, y);
    }
}
