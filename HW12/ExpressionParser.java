package expression;

import expression.exceptions.*;

import java.util.EmptyStackException;
import java.util.Stack;

public class ExpressionParser implements Parser {
    private Tokenizer tokenizer;

    private int priority(Component x) {
        switch (x) {
            case MAX:
            case MIN:
                return 4;

            case NEGATE:
            case ABS:
            case SQRT:
                return 0;

            case ADD:
            case SUB:
                return 3;

            case MUL:
            case DIV:
                return 2;

            default:
                return 5;
        }
    }

    private void calculate(Stack<CommonExpression> elements, Stack<Component> operations) throws ParsingException, EvaluateException {
        try {
            CommonExpression insertion = null;

            CommonExpression first = elements.peek();
            elements.pop();

            if (priority(operations.peek()) == 0) {
                switch (operations.peek()) {
                    case NEGATE:
                        insertion = new CheckedNegate(first);
                        break;

                    case ABS:
                        insertion = new Abs(first);
                        break;

                    case SQRT:
                        insertion = new Sqrt(first);
                        break;
                }
            } else {
                CommonExpression second = elements.peek();
                elements.pop();

                switch (operations.peek()) {
                    case ADD:
                        insertion = new CheckedAdd(second, first);
                        break;

                    case SUB:
                        insertion = new CheckedSubtract(second, first);
                        break;

                    case MUL:
                        insertion = new CheckedMultiply(second, first);
                        break;

                    case DIV:
                        insertion = new CheckedDivide(second, first);
                        break;

                    case MIN:
                        insertion = new Min(second, first);
                        break;

                    case MAX:
                        insertion = new Max(second, first);
                        break;
                }
            }
            elements.push(insertion);
            operations.pop();
        } catch (EmptyStackException e) {
            throw new MissingOperandException("Expression does not have enough operands");
        } catch (Exception e) {
            throw e;
        }
    }

    private CommonExpression recursiveParse() throws ParsingException, EvaluateException {
        Stack<CommonExpression> elements = new Stack<>();
        Stack<Component> operations = new Stack<>();
        StringBuilder curConst = new StringBuilder();

        while(true) {
            try {
                Component curComp = tokenizer.getNextComponent(curConst);

                switch (curComp) {
                    case ADD:
                    case SUB:
                    case MUL:
                    case DIV:
                    case MIN:
                    case MAX:
                        while (!operations.empty() && priority(operations.peek()) <= priority(curComp)) {
                            calculate(elements, operations);
                        }
                        operations.push(curComp);
                        tokenizer.setUnary(true);
                        break;

                    case NEGATE:
                    case ABS:
                    case SQRT:
                        operations.push(curComp);
                        tokenizer.setUnary(true);
                        break;

                    case CONST:
                        elements.push(new Const(tokenizer.getConst(curConst)));
                        tokenizer.setUnary(false);
                        break;

                    case VARIABLE:
                        elements.push(new Variable(tokenizer.getCurrentVariable()));
                        tokenizer.setUnary(false);
                        break;

                    case OPEN_BRACE:
                        tokenizer.setUnary(true);
                        elements.push(recursiveParse());
                        break;

                    default:
                        while (!operations.empty()) {
                            calculate(elements, operations);
                        }
                        tokenizer.setUnary(false);
                        if (elements.size() > 1)
                            throw new MissingOperationException("Expression does not have enough operations");
                        if (elements.size() == 0)
                            throw new MissingOperandException("Expression does not have enough operands");
                        return elements.peek();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public CommonExpression parse(String expression) throws ParsingException, EvaluateException {
        tokenizer = new Tokenizer(expression);

        try {
            return recursiveParse();
        } catch(Exception e) {
            throw e;
        }
    }
}
