package expression;

import calculators.Calculator;
import expression.exceptions.EvaluateException;

public abstract class BinaryFunction<T> implements TripleExpression<T> {
    private TripleExpression<T> arg1, arg2;
    protected Calculator<T> calculator;

    public BinaryFunction(TripleExpression<T> arg1, TripleExpression<T> arg2, Calculator<T> calculator) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.calculator = calculator;
    }

    public abstract T calc(T x, T y) throws EvaluateException;

    public T evaluate(T x, T y, T z) throws EvaluateException {
        return calc(arg1.evaluate(x, y, z), arg2.evaluate(x, y, z));
    }
}
