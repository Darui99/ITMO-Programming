package expression;

import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public class CheckedAdd extends BinaryFunction implements CommonExpression {
    public CheckedAdd(CommonExpression a1, CommonExpression a2) {
        arg1 = a1;
        arg2 = a2;
    }

    public int calc(int x, int y) throws EvaluateException {
        if (y >= 0 && x > Integer.MAX_VALUE - y)
            throw new OverflowException("Overflow while adding");
        if (y < 0 && x < Integer.MIN_VALUE - y)
            throw new OverflowException("Overflow while adding");
        return x + y;
    }
}
