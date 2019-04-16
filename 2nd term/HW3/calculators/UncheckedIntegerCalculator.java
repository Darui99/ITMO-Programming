package calculators;

import expression.exceptions.*;

public class UncheckedIntegerCalculator implements Calculator<Integer> {

    public Integer add(Integer arg1, Integer arg2) throws EvaluateException {
        return arg1 + arg2;
    }

    public Integer sub(Integer arg1, Integer arg2) throws EvaluateException {
        return arg1 - arg2;
    }

    public Integer mul(Integer arg1, Integer arg2) throws EvaluateException {
        return arg1 * arg2;
    }

    public Integer div(Integer arg1, Integer arg2) throws EvaluateException {
        return arg1 / arg2;
    }

    public Integer neg(Integer arg) throws EvaluateException {
        return -arg;
    }

    public Integer abs(Integer arg) throws EvaluateException {
        return Math.abs(arg);
    }

    public Integer sqr(Integer arg) throws EvaluateException {
        return mul(arg, arg);
    }

    public Integer mod(Integer arg1, Integer arg2) throws EvaluateException {
        int x = arg1, y = arg2;
        if (y == 0)
            throw new DBZException("Division by zero");
        return x % y;
    }

    public Integer convertString(String s) throws Exception {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            throw e;
        }
    }
}
