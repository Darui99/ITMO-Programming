package calculators;

import expression.exceptions.DBZException;
import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public class FloatCalculator implements Calculator<Float> {

    public Float add(Float arg1, Float arg2) throws EvaluateException {
        return arg1 + arg2;
    }

    public Float sub(Float arg1, Float arg2) throws EvaluateException {
        return arg1 - arg2;
    }

    public Float mul(Float arg1, Float arg2) throws EvaluateException {
        return arg1 * arg2;
    }

    public Float div(Float arg1, Float arg2) throws EvaluateException {
        return arg1 / arg2;
    }

    public Float neg(Float arg) throws EvaluateException {
        return -arg;
    }

    public Float abs(Float arg) throws EvaluateException {
        return Math.abs(arg);
    }

    public Float sqr(Float arg) throws EvaluateException {
        return mul(arg, arg);
    }

    public Float mod (Float arg1, Float arg2) throws EvaluateException {
        return arg1 % arg2;
    }

    public Float convertString(String s) throws Exception {
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            throw e;
        }
    }
}
