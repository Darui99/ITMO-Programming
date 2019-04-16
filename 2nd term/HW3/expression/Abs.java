package expression;

import calculators.Calculator;
import expression.exceptions.EvaluateException;

public class Abs<T> extends UnaryFunction<T> {

    public Abs(TripleExpression<T> arg, Calculator<T> c) {
        super(arg, c);
    }

    public T calc(T x) throws EvaluateException {
        return calculator.abs(x);
    }
}
