package expression;

import calculators.Calculator;
import expression.exceptions.EvaluateException;

public class Negate<T> extends UnaryFunction<T> {

    public Negate(TripleExpression<T> arg, Calculator<T> c) {
        super(arg, c);
    }

    public T calc(T x) throws EvaluateException {
        return calculator.neg(x);
    }
}
