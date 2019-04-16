package expression;

import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public interface TripleExpression {
    int evaluate(int x, int y, int z) throws EvaluateException;
}
