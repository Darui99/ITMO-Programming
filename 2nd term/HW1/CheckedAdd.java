package expression;

import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public class CheckedAdd extends BinaryFunction {
    public CheckedAdd(TripleExpression a1, TripleExpression a2) {
        super(a1, a2);
    }

    public int calc(int x, int y) throws EvaluateException {
        if (y >= 0 && x > Integer.MAX_VALUE - y)
            throw new OverflowException("Overflow while adding");
        if (y < 0 && x < Integer.MIN_VALUE - y)
            throw new OverflowException("Overflow while adding");
        return x + y;
    }
}
