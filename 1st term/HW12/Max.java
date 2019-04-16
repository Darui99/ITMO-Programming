package expression;

public class Max extends BinaryFunction implements CommonExpression {
    public Max(CommonExpression a1, CommonExpression a2) {
        arg1 = a1;
        arg2 = a2;
    }

    public int calc(int x, int y) {
        if (x > y)
            return x;
        else
            return y;
    }
}
