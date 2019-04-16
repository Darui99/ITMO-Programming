package expression;

import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public abstract class BinaryFunction implements CommonExpression {
    protected CommonExpression arg1, arg2;

    public BinaryFunction() {
        arg1 = null;
        arg2 = null;
    }

    public abstract int calc(int x, int y) throws EvaluateException;

    public int evaluate(int x, int y, int z) throws EvaluateException {
        return calc(arg1.evaluate(x, y, z), arg2.evaluate(x, y, z));
    }
}
