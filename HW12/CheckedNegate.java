package expression;

import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public class CheckedNegate extends UnaryFunction implements CommonExpression {
    public CheckedNegate(CommonExpression x) {
        arg = x;
    }

    public int calc(int x) throws EvaluateException {
        if (x == Integer.MIN_VALUE)
            throw new OverflowException("Overflow while negating");
        return -x;
    }
}