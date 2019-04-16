package expression;

import calculators.Calculator;
import expression.exceptions.EvaluateException;

public abstract class UnaryFunction<T> implements TripleExpression<T> {
    private TripleExpression<T> arg;
    protected Calculator<T> calculator;

    public UnaryFunction(TripleExpression<T> arg, Calculator<T> calculator) {
        this.arg = arg;
        this.calculator = calculator;
    }

    public abstract T calc(T x) throws EvaluateException;

    public T evaluate(T x, T y, T z) throws EvaluateException {
        return calc(arg.evaluate(x, y, z));
    }
}
