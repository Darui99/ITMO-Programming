package expression;

import calculators.*;
import expression.generic.Tabulator;

import java.util.HashMap;

public class GenericTabulator implements Tabulator {
    private HashMap<String, Calculator> allowedModes;

    public GenericTabulator() {
        allowedModes = new HashMap<>();

        allowedModes.put("i", new IntegerCalculator());
        allowedModes.put("d", new DoubleCalculator());
        allowedModes.put("bi", new BigIntegerCalculator());
        allowedModes.put("u", new UncheckedIntegerCalculator());
        allowedModes.put("b", new ByteCalculator());
        allowedModes.put("f", new FloatCalculator());
    }

    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        Calculator<?> calculator = allowedModes.get(mode);
        return genTable(calculator, expression, x1, x2, y1, y2, z1, z2);
    }

    private <T> Object[][][] genTable(Calculator<T> calculator, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        Object[][][] res = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        ExpressionParser<T> parser = new ExpressionParser<>(calculator);
        TripleExpression<T> exp;
        try {
            exp = parser.parse(expression);
        } catch (Exception e) {
            return res;
        }

        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[i].length; j++) {
                for (int k = 0; k < res[i][j].length; k++) {
                    try {
                        String x = String.valueOf(x1 + i);
                        String y = String.valueOf(y1 + j);
                        String z = String.valueOf(z1 + k);
                        res[i][j][k] = exp.evaluate(calculator.convertString(x), calculator.convertString(y), calculator.convertString(z));
                    } catch (Exception e) {
                        res[i][j][k] = null;
                    }
                }
            }
        }
        return res;
    }
}
