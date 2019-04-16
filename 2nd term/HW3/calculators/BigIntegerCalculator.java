package calculators;

import expression.exceptions.DBZException;
import expression.exceptions.EvaluateException;

import java.math.BigInteger;

public class BigIntegerCalculator implements Calculator<BigInteger> {

    public BigInteger add(BigInteger arg1, BigInteger arg2) {
        return arg1.add(arg2);
    }

    public BigInteger sub(BigInteger arg1, BigInteger arg2) {
        return arg1.subtract(arg2);
    }

    public BigInteger mul(BigInteger arg1, BigInteger arg2) {
        return arg1.multiply(arg2);
    }

    public BigInteger div(BigInteger arg1, BigInteger arg2) throws EvaluateException {
        if (arg2.equals(BigInteger.ZERO))
            throw new DBZException("Division by zero");
        return arg1.divide(arg2);
    }

    public BigInteger neg(BigInteger arg) {
        return BigInteger.ZERO.subtract(arg);
    }

    public BigInteger abs(BigInteger arg) {
        return arg.abs();
    }

    public BigInteger sqr(BigInteger arg) {
        return mul(arg, arg);
    }

    public BigInteger mod(BigInteger arg1, BigInteger arg2) throws EvaluateException {
        if (arg2.equals(BigInteger.ZERO))
            throw new DBZException("Division by zero");
        return arg1.mod(arg2);
    }

    public BigInteger convertString(String s) {
        return new BigInteger(s);
    }
}
