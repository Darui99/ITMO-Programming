package expression;

import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public class Abs extends UnaryFunction implements CommonExpression {
    public Abs(CommonExpression x) {
        arg = x;
    }

    public int calc(int x) throws EvaluateException {
        if (x >= 0)
            return x;
        if (x == Integer.MIN_VALUE)
            throw new OverflowException("Overflow while abs");
        return -x;
    }
}
