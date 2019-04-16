package expression;

import calculators.Calculator;
import expression.exceptions.EvaluateException;

public class Sqr<T> extends UnaryFunction<T> {

    public Sqr(TripleExpression<T> arg, Calculator<T> c) {
        super(arg, c);
    }

    public T calc(T x) throws EvaluateException {
        return calculator.sqr(x);
    }
}
