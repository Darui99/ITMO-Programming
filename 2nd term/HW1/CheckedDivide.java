package expression;

import expression.exceptions.DBZException;
import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public class CheckedDivide extends BinaryFunction {
    public CheckedDivide(TripleExpression a1, TripleExpression a2) {
        super(a1, a2);
    }

    public int calc(int x, int y) throws EvaluateException {
        if (x == Integer.MIN_VALUE && y == -1)
            throw new OverflowException("Overflow while division");
        if (y == 0)
            throw new DBZException("Division by zero");
        return x / y;
    }
}
