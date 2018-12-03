package expression;

import java.util.Stack;

public class ExpressionParser implements Parser {
    private enum Component {CONST, VARIABLE, ADD, SUB, UNARY_MINUS, MUL, DIV, AND, XOR, OR, NOT, CNT, OPEN_BRACE, CLOSE_BRACE, DEFAULT}

    private int i;
    private Component curComp;
    private String s, curVariable;
    private boolean uniMinus;

    private void fillConst(StringBuilder curConst) {
        while (i < s.length() && Character.isDigit(s.charAt(i))) {
            curConst.append(s.charAt(i));
            i++;
        }
        i--;
    }

    private int getConst(StringBuilder curConst) {
        int res = Integer.parseInt(curConst.toString());
        curConst.delete(0, curConst.length());
        return res;
    }

    private void nextComponent(StringBuilder curConst) {
        while(i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        if (i == s.length()) {
            curComp = Component.DEFAULT;
            return;
        }

        switch (s.charAt(i)) {
            case '(':
                curComp = Component.OPEN_BRACE;
                break;

            case ')':
                curComp = Component.CLOSE_BRACE;
                break;

            case '+':
                curComp = Component.ADD;
                break;

            case '*':
                curComp = Component.MUL;
                break;

            case '/':
                curComp = Component.DIV;
                break;

            case '&':
                curComp = Component.AND;
                break;

            case '^':
                curComp = Component.XOR;
                break;

            case '|':
                curComp = Component.OR;
                break;

            case '~':
                curComp = Component.NOT;
                break;

            default:
                if (Character.isLetter(s.charAt(i))) {
                    if (s.charAt(i) == 'x' || s.charAt(i) == 'y' || s.charAt(i) == 'z') {
                        curComp = Component.VARIABLE;
                        curVariable = String.valueOf(s.charAt(i));
                    } else {
                        i += 4;
                        curComp = Component.CNT;
                    }
                } else {
                    if (Character.isDigit(s.charAt(i))) {
                        fillConst(curConst);
                        curComp = Component.CONST;
                    } else {
                        if (uniMinus) {
                            if (Character.isDigit(s.charAt(i + 1))) {
                                curConst.append(s.charAt(i++));
                                fillConst(curConst);
                                curComp = Component.CONST;
                            } else {
                                curComp = Component.UNARY_MINUS;
                            }
                        } else {
                            curComp = Component.SUB;
                        }
                    }
                }
        }
        i++;
    }

    private int priority(Component x) {
        switch (x) {
            case NOT:
            case CNT:
            case UNARY_MINUS:
                return -1;

            case ADD:
            case SUB:
                return 1;

            case AND:
                return 2;

            case XOR:
                return 3;

            case OR:
                return 4;

            case MUL:
            case DIV:
                return 0;

            default:
                return 5;
        }
    }

    private void calculate(Stack<CommonExpression> elements, Stack<Component> operations) {
        CommonExpression insertion = null;

        CommonExpression first = elements.peek();
        elements.pop();

        if (priority(operations.peek()) == -1) {
            switch (operations.peek()) {
                case NOT:
                    insertion = new Not(first);
                    break;

                case CNT:
                    insertion = new Count(first);
                    break;

                case UNARY_MINUS:
                    insertion = new UnaryMinus(first);
                    break;
            }
        } else {
            CommonExpression second = elements.peek();
            elements.pop();

            switch (operations.peek()) {
                case ADD:
                    insertion = new Add(second, first);
                    break;

                case SUB:
                    insertion = new Subtract(second, first);
                    break;

                case MUL:
                    insertion = new Multiply(second, first);
                    break;

                case DIV:
                    insertion = new Divide(second, first);
                    break;

                case AND:
                    insertion = new And(second, first);
                    break;

                case XOR:
                    insertion = new Xor(second, first);
                    break;

                case OR:
                    insertion = new Or(second, first);
                    break;
            }
        }
        elements.push(insertion);
        operations.pop();
    }

    private CommonExpression recursiveParse() {
        Stack<CommonExpression> elements = new Stack<>();
        Stack<Component> operations = new Stack<>();
        StringBuilder curConst = new StringBuilder();

        while(true) {
            nextComponent(curConst);

            switch (curComp) {
                case ADD:
                case SUB:
                case MUL:
                case DIV:
                case AND:
                case XOR:
                case OR:
                    while (!operations.empty() && priority(operations.peek()) <= priority(curComp)) {
                        calculate(elements, operations);
                    }
                    operations.push(curComp);
                    uniMinus = true;
                    break;

                case NOT:
                case CNT:
                case UNARY_MINUS:
                    operations.push(curComp);
                    uniMinus = true;
                    break;

                case CONST:
                    elements.push(new Const(getConst(curConst)));
                    uniMinus = false;
                    break;

                case VARIABLE:
                    elements.push(new Variable(curVariable));
                    uniMinus = false;
                    break;

                case OPEN_BRACE:
                    uniMinus = true;
                    elements.push(recursiveParse());
                    break;

                default:
                    while(!operations.empty()) {
                        calculate(elements, operations);
                    }
                    uniMinus = false;
                    return elements.peek();
            }
        }
    }

    public CommonExpression parse(String expression) {
        i = 0;
        curComp = Component.DEFAULT;
        s = expression;
        uniMinus = true;

        return recursiveParse();
    }
}
