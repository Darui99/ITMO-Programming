package expression;

import expression.exceptions.EvaluateException;

public abstract class UnaryFunction implements TripleExpression {
    private TripleExpression arg;

    public UnaryFunction(TripleExpression a) {
        arg = a;
    }

    public abstract int calc(int x) throws EvaluateException;

    public int evaluate(int x, int y, int z) throws EvaluateException {
        return calc(arg.evaluate(x, y, z));
    }
}
