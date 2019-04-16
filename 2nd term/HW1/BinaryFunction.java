package expression;

import expression.exceptions.EvaluateException;

public abstract class BinaryFunction implements TripleExpression {
    private TripleExpression arg1, arg2;

    public BinaryFunction(TripleExpression arg1, TripleExpression arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public abstract int calc(int x, int y) throws EvaluateException;

    public int evaluate(int x, int y, int z) throws EvaluateException {
        return calc(arg1.evaluate(x, y, z), arg2.evaluate(x, y, z));
    }
}
