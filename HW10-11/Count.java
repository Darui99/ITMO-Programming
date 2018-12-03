package expression;

public class Count extends UnaryFunction implements CommonExpression {
    public Count(CommonExpression x) {
        arg = x;
    }

    private int bitCount(int x) {
        int res = 0;
        while (x != 0) {
            res++;
            x &= (x - 1);
        }
        return res;
    }

    public int calc(int x) {
        return bitCount(x);
    }

    public double calc(double x) {
        return calc((int)x);
    }
}
