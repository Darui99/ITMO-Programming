package expression;

import expression.exceptions.EvaluateException;
import expression.exceptions.IncorrectFunctionArgument;

public class Sqrt extends UnaryFunction {
    public Sqrt(TripleExpression x) {
        super(x);
    }

    public int calc(int x) throws EvaluateException {
        if (x < 0)
            throw new IncorrectFunctionArgument("Sqrt has incorrect argument");
        int lb = 0, rb = 46350;
        while (rb - lb > 1) {
            int mid = (lb + rb) / 2;
            if (mid * mid <= x)
                lb = mid;
            else
                rb = mid;
        }
        return lb;
    }
}