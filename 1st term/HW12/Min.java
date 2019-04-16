package expression;

public class Min extends BinaryFunction implements CommonExpression {
    public Min(CommonExpression a1, CommonExpression a2) {
        arg1 = a1;
        arg2 = a2;
    }

    public int calc(int x, int y) {
        if (x < y)
            return x;
        else
            return y;
    }
}
