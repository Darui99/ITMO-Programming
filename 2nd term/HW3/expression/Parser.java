package expression;

import expression.exceptions.EvaluateException;
import expression.exceptions.ParsingException;

public interface Parser<T> {
    TripleExpression<T> parse(String expression) throws EvaluateException, ParsingException;
}
