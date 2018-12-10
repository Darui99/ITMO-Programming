package expression;

import expression.exceptions.DBZException;
import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public class CheckedDivide extends BinaryFunction implements CommonExpression {
    public CheckedDivide(CommonExpression a1, CommonExpression a2) {
        arg1 = a1;
        arg2 = a2;
    }

    public int calc(int x, int y) throws EvaluateException {
        if (x == Integer.MIN_VALUE && y == -1)
            throw new OverflowException("Overflow while dividing");
        if (y == 0)
            throw new DBZException("Dividing by zero");
        return x / y;
    }
}
