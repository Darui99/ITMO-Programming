package expression;

import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public class CheckedMultiply extends BinaryFunction implements CommonExpression {
    public CheckedMultiply(CommonExpression a1, CommonExpression a2) {
        arg1 = a1;
        arg2 = a2;
    }

    private int sign(int x) {
        if (x < 0)
            return 0;
        else
            return 1;
    }

    public int calc(int x, int y) throws EvaluateException {
        if (x > y) {
            int t = y;
            y = x;
            x = t;
        }
        if (x == Integer.MIN_VALUE && y != 0 && y != 1)
            throw new OverflowException("Overflow while multiplying");
        else {
            if (y == Integer.MAX_VALUE && x != -1 && x != 0 && x != 1)
                throw new OverflowException("Overflow while multiplying");
            else if (x != 0 && y != 0) {
                if (sign(x) == sign(y) && sign(x) == 1 && x > Integer.MAX_VALUE / y)
                    throw new OverflowException("Overflow while multiplying");
                if (sign(x) == sign(y) && sign(x) == 0 && x < Integer.MAX_VALUE / y)
                    throw new OverflowException("Overflow while multiplying");
                if (sign(x) != sign(y) && x < Integer.MIN_VALUE / y)
                    throw new OverflowException("Overflow while multiplying");
            }
        }
        return x * y;
    }
}
