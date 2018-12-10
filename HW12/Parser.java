package expression;

import expression.exceptions.EvaluateException;
import expression.exceptions.ParsingException;

public interface Parser {
    TripleExpression parse(String expression) throws EvaluateException, ParsingException;
}
