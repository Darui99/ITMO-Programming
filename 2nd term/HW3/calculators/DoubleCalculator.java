package calculators;

import expression.exceptions.DBZException;
import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public class DoubleCalculator implements Calculator<Double> {

    public Double add(Double arg1, Double arg2) throws EvaluateException {
        return arg1 + arg2;
    }

    public Double sub(Double arg1, Double arg2) throws EvaluateException {
        return arg1 - arg2;
    }

    public Double mul(Double arg1, Double arg2) throws EvaluateException {
        return arg1 * arg2;
    }

    public Double div(Double arg1, Double arg2) throws EvaluateException {
        return arg1 / arg2;
    }

    public Double neg(Double arg) throws EvaluateException {
        return -arg;
    }

    public Double abs(Double arg) throws EvaluateException {
        return Math.abs(arg);
    }

    public Double sqr(Double arg) throws EvaluateException {
        return mul(arg, arg);
    }

    public Double mod (Double arg1, Double arg2) throws EvaluateException {
        return arg1 % arg2;
    }

    public Double convertString(String s) throws Exception {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            throw e;
        }
    }
}
