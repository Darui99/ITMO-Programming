package calculators;

import expression.exceptions.*;

public class ByteCalculator implements Calculator<Byte> {

    public Byte add(Byte arg1, Byte arg2) throws EvaluateException {
        return (byte)(arg1 + arg2);
    }

    public Byte sub(Byte arg1, Byte arg2) throws EvaluateException {
        return (byte)(arg1 - arg2);
    }

    public Byte mul(Byte arg1, Byte arg2) throws EvaluateException {
        return (byte)(arg1 * arg2);
    }

    public Byte div(Byte arg1, Byte arg2) throws EvaluateException {
        return (byte)(arg1 / arg2);
    }

    public Byte neg(Byte arg) throws EvaluateException {
        return (byte)(-arg);
    }

    public Byte abs(Byte arg) throws EvaluateException {
        return (byte)Math.abs(arg);
    }

    public Byte sqr(Byte arg) throws EvaluateException {
        return mul(arg, arg);
    }

    public Byte mod(Byte arg1, Byte arg2) throws EvaluateException {
        byte x = arg1, y = arg2;
        if (y == 0)
            throw new DBZException("Division by zero");
        return (byte)(x % y);
    }

    public Byte convertString(String s) throws Exception {
        try {
            return (byte)Integer.parseInt(s);
        } catch (Exception e) {
            throw e;
        }
    }
}
