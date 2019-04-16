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
                return 3;

            case ABS:
            case SQRT:
            case HIGH:
            case LOW:
                return 0;

            case ADD:
            case SUB:
                return 2;

            case MUL:
            case DIV:
                return 1;

            default:
                return 4;
        }
    }

    private void calculate(Stack<TripleExpression> elements, Stack<Component> operations, Stack<Integer> priorities, Stack<Integer> positions) throws ParsingException, EvaluateException {
        try {
            TripleExpression insertion = null;

            TripleExpression first = elements.peek();
            elements.pop();

            if (priorities.peek() == 0) {
                switch (operations.peek()) {
                    case SUB:
                        insertion = new CheckedNegate(first);
                        break;

                    case ABS:
                        insertion = new Abs(first);
                        break;

                    case SQRT:
                        insertion = new Sqrt(first);
                        break;

                    case HIGH:
                        insertion = new High(first);
                        break;

                    case LOW:
                        insertion = new Low(first);
                        break;
                }
            } else {
                TripleExpression second = elements.peek();
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
            positions.pop();
            priorities.pop();
        } catch (EmptyStackException e) {
            throw new MissingOperandException("Expression does not have enough operands", positions.peek()); // !!!!!!!!
        } catch (Exception e) {
            throw e;
        }
    }

    private TripleExpression recursiveParse() throws ParsingException, EvaluateException {
        Stack<TripleExpression> elements = new Stack<>();
        Stack<Component> operations = new Stack<>();
        Stack<Integer> priorities = new Stack<>(), positions = new Stack<>();
        StringBuilder curConst = new StringBuilder();

        while(true) {
            try {
                Component curComp = tokenizer.getNextComponent(curConst);
                int curPos = tokenizer.getPosition();

                switch (curComp) {
                    case ADD:
                    case MUL:
                    case DIV:
                    case MIN:
                    case MAX:
                        while (!operations.empty() && priorities.peek() <= priority(curComp)) {
                            calculate(elements, operations, priorities, positions);
                        }
                        operations.push(curComp);
                        priorities.push(priority(curComp));
                        positions.push(curPos);
                        tokenizer.setUnary(true);
                        break;

                    case ABS:
                    case SQRT:
                    case HIGH:
                    case LOW:
                        operations.push(curComp);
                        priorities.push(priority(curComp));
                        positions.push(curPos);
                        tokenizer.setUnary(true);
                        break;

                    case SUB:
                        if (tokenizer.getUnary()) {
                            operations.push(curComp);
                            priorities.push(0);
                            positions.push(curPos);
                            tokenizer.setUnary(true);
                            break;
                        } else {
                            while (!operations.empty() && priorities.peek() <= priority(curComp)) {
                                calculate(elements, operations, priorities, positions);
                            }
                            operations.push(curComp);
                            priorities.push(priority(curComp));
                            positions.push(curPos);
                            tokenizer.setUnary(true);
                            break;
                        }

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
                            calculate(elements, operations, priorities, positions);
                        }
                        tokenizer.setUnary(false);
                        if (elements.size() > 1) {
                            throw new MissingOperationException("Expression does not have enough operations");
                        }
                        if (elements.size() == 0)
                            throw new MissingOperandException("Expression does not have enough operands", positions.peek());
                        return elements.peek();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public TripleExpression parse(String expression) throws ParsingException, EvaluateException {
        tokenizer = new Tokenizer(expression);

        try {
            return recursiveParse();
        } catch(Exception e) {
            throw e;
        }
    }
}
