package expression;

import expression.exceptions.*;

import java.util.HashMap;

public class Tokenizer {
    private int i, braceBalance;
    private Component curComp;
    private String s, curVariable;
    private boolean unary;
    HashMap<String, Component> allowedOperations;

    public Tokenizer(String expression) {
        i = 0;
        braceBalance = 0;
        curComp = Component.DEFAULT;
        s = expression;
        unary = true;

        allowedOperations = new HashMap<>();
        allowedOperations.put("x", Component.VARIABLE);
        allowedOperations.put("y", Component.VARIABLE);
        allowedOperations.put("z", Component.VARIABLE);
        allowedOperations.put("abs", Component.ABS);
        allowedOperations.put("sqrt", Component.SQRT);
        allowedOperations.put("min", Component.MIN);
        allowedOperations.put("max", Component.MAX);
    }

    private String getNextOperation() {
        StringBuilder res = new StringBuilder();
        while(i < s.length() && (Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)))) {
            res.append(s.charAt(i));
            i++;
        }
        i--;
        return res.toString();
    }

    private void fillConst(StringBuilder curConst) {
        while (i < s.length() && Character.isDigit(s.charAt(i))) {
            curConst.append(s.charAt(i));
            i++;
        }
        i--;
    }

    public int getConst(StringBuilder curConst) throws ParsingException {
        try {
            int res = Integer.parseInt(curConst.toString());
            curConst.delete(0, curConst.length());
            return res;
        } catch (Exception e) {
            throw new IncorrectConstantException("Expression has incorrect constant");
        }
    }

    private void nextComponent(StringBuilder curConst) throws ParsingException {
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
                braceBalance++;
                break;

            case ')':
                curComp = Component.CLOSE_BRACE;
                braceBalance--;
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

            default:
                if (Character.isLetter(s.charAt(i))) {
                    String curOperation = getNextOperation();
                    if (allowedOperations.containsKey(curOperation)) {
                        curComp = allowedOperations.get(curOperation);
                        if (curComp == Component.VARIABLE)
                            curVariable = String.valueOf(s.charAt(i));
                    } else
                        throw new IncorrectTokenException("Expression has incorrect token");
                } else {
                    if (Character.isDigit(s.charAt(i))) {
                        fillConst(curConst);
                        curComp = Component.CONST;
                    } else {
                        if (s.charAt(i) == '-') {
                            if (unary) {
                                if (i + 1 == s.length())
                                    throw new MissingOperandException("Expression does not have enough operands");
                                if (Character.isDigit(s.charAt(i + 1))) {
                                    curConst.append(s.charAt(i++));
                                    fillConst(curConst);
                                    curComp = Component.CONST;
                                } else {
                                    curComp = Component.NEGATE;
                                }
                            } else {
                                curComp = Component.SUB;
                            }
                        } else {
                            throw new IncorrectTokenException("Expression has incorrect token");
                        }
                    }
                }
        }
        i++;
    }

    public Component getNextComponent(StringBuilder curConst) throws ParsingException {
        nextComponent(curConst);
        if (braceBalance < 0)
            throw new CBSException("Expression has incorrect brace sequence");
        if (curComp == Component.DEFAULT && braceBalance != 0)
            throw new CBSException("Expression has incorrect brace sequence");
        return curComp;
    }

    public String getCurrentVariable() {
        return curVariable;
    }

    public void setUnary(boolean val) {
        unary = val;
    }
}
