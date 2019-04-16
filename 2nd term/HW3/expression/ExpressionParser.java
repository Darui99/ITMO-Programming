package expression;

import calculators.Calculator;
import expression.exceptions.*;

import java.util.EmptyStackException;
import java.util.Stack;

public class ExpressionParser<T> implements Parser<T> {
    private Tokenizer<T> tokenizer;
    private Calculator<T> calculator;

    public ExpressionParser(Calculator<T> calculator) {
        this.calculator = calculator;
    }

    private int priority(Component x) {
        switch (x) {
            case ABS:
            case SQR:
                return 0;

            case ADD:
            case SUB:
                return 2;

            case MUL:
            case DIV:
            case MOD:
                return 1;

            default:
                return 3;
        }
    }

    private void calculate(Stack<TripleExpression<T>> elements, Stack<Component> operations, Stack<Integer> priorities, Stack<Integer> positions) throws ParsingException {
        try {
            TripleExpression<T> insertion = null;

            TripleExpression<T> first = elements.peek();
            elements.pop();

            if (priorities.peek() == 0) {
                switch (operations.peek()) {
                    case SUB:
                        insertion = new Negate<T>(first, calculator);
                        break;

                    case ABS:
                        insertion = new Abs<>(first, calculator);
                        break;

                    case SQR:
                        insertion = new Sqr<>(first, calculator);
                }
            } else {
                TripleExpression<T> second = elements.peek();
                elements.pop();

                switch (operations.peek()) {
                    case ADD:
                        insertion = new Add<>(second, first, calculator);
                        break;

                    case SUB:
                        insertion = new Subtract<>(second, first, calculator);
                        break;

                    case MUL:
                        insertion = new Multiply<>(second, first, calculator);
                        break;

                    case DIV:
                        insertion = new Divide<>(second, first, calculator);
                        break;

                    case MOD:
                        insertion = new Mod<>(second, first, calculator);
                }
            }
            elements.push(insertion);
            operations.pop();
            positions.pop();
            priorities.pop();
        } catch (EmptyStackException e) {
            throw new MissingOperandException("Expression does not have enough operands", positions.peek());
        } catch (Exception e) {
            throw e;
        }
    }

    private TripleExpression<T> recursiveParse() throws ParsingException, EvaluateException {
        Stack<TripleExpression<T>> elements = new Stack<>();
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
                    case MOD:
                        while (!operations.empty() && priorities.peek() <= priority(curComp)) {
                            calculate(elements, operations, priorities, positions);
                        }
                        operations.push(curComp);
                        priorities.push(priority(curComp));
                        positions.push(curPos);
                        tokenizer.setUnary(true);
                        break;

                    case ABS:
                    case SQR:
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
                        elements.push(new Const<>(tokenizer.getConst(curConst)));
                        tokenizer.setUnary(false);
                        break;

                    case VARIABLE:
                        elements.push(new Variable<>(tokenizer.getCurrentVariable()));
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

    public TripleExpression<T> parse(String expression) throws ParsingException, EvaluateException {
        tokenizer = new Tokenizer<>(expression, calculator);

        try {
            return recursiveParse();
        } catch(Exception e) {
            throw e;
        }
    }
}
