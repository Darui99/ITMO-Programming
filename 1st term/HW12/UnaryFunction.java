package expression;

import expression.exceptions.EvaluateException;

public abstract class UnaryFunction implements CommonExpression {
    protected CommonExpression arg;

    public UnaryFunction() {
        arg = null;
    }

    public abstract int calc(int x) throws EvaluateException;

    public int evaluate(int x, int y, int z) throws EvaluateException {
        return calc(arg.evaluate(x, y, z));
    }
}
