package calculators;

import expression.exceptions.*;

public class IntegerCalculator implements Calculator<Integer> {

    private int sign(int x) {
        if (x < 0) {
            return 0;
        }
        else {
            return 1;
        }
    }

    public Integer add(Integer arg1, Integer arg2) throws EvaluateException {
        int x = arg1, y = arg2;
        if (y >= 0 && x > Integer.MAX_VALUE - y)
            throw new OverflowException("Overflow while adding");
        if (y < 0 && x < Integer.MIN_VALUE - y)
            throw new OverflowException("Overflow while adding");
        return x + y;
    }

    public Integer sub(Integer arg1, Integer arg2) throws EvaluateException {
        int x = arg1, y = arg2;
        if (y >= 0 && x < Integer.MIN_VALUE + y)
            throw new OverflowException("Overflow while subtracting");
        if (y < 0 && x > Integer.MAX_VALUE + y)
            throw new OverflowException("Overflow while subtracting");
        return x - y;
    }

    public Integer mul(Integer arg1, Integer arg2) throws EvaluateException {
        int x = arg1, y = arg2;
        if (x > y) {
            int t = y;
            y = x;
            x = t;
        }
        if (x == Integer.MIN_VALUE && y != 0 && y != 1)
            throw new OverflowException("Overflow while multiplying");
        else {
            if (y == Integer.MAX_VALUE && x != -1 && x != 0 && x != 1)
                throw new OverflowException("Overflow while multiplying");
            else if (x != 0 && y != 0) {
                if (sign(x) == sign(y) && sign(x) == 1 && x > Integer.MAX_VALUE / y)
                    throw new OverflowException("Overflow while multiplying");
                if (sign(x) == sign(y) && sign(x) == 0 && x < Integer.MAX_VALUE / y)
                    throw new OverflowException("Overflow while multiplying");
                if (sign(x) != sign(y) && x < Integer.MIN_VALUE / y)
                    throw new OverflowException("Overflow while multiplying");
            }
        }
        return x * y;
    }

    public Integer div(Integer arg1, Integer arg2) throws EvaluateException {
        int x = arg1, y = arg2;
        if (x == Integer.MIN_VALUE && y == -1)
            throw new OverflowException("Overflow while division");
        if (y == 0)
            throw new DBZException("Division by zero");
        return x / y;
    }

    public Integer neg(Integer arg) throws EvaluateException {
        int x = arg;
        if (x == Integer.MIN_VALUE)
            throw new OverflowException("Overflow while negating");
        return -x;
    }

    public Integer abs(Integer arg) throws EvaluateException {
        int x = arg;
        if (x >= 0)
            return x;
        if (x == Integer.MIN_VALUE)
            throw new OverflowException("Overflow while abs");
        return -x;
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
