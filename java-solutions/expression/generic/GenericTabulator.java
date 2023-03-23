package expression.generic;

import java.math.BigInteger;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        var evaluator = switch (mode) {
            case "i" -> new ExpressionIntEvaluator<>(getCheckedIntegerParser().parse(expression), (a) -> a);
            case "d" -> new ExpressionIntEvaluator<>(getDoubleParser().parse(expression), (a) -> (double) a);
            case "bi" ->
                    new ExpressionIntEvaluator<>(getBigIntegerParser().parse(expression), (a) -> new BigInteger(String.valueOf(a)));
            default -> throw new IllegalArgumentException("incorrect mode");
        };

        var xSize = x2 - x1 + 1;
        var ySize = y2 - y1 + 1;
        var zSize = z2 - z1 + 1;

        Object[][][] result = new Object[xSize][ySize][zSize];

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                for (int k = 0; k < zSize; k++) {
                    try {
                        result[i][j][k] = evaluator.evaluate(
                                x1 + i,
                                j + y1,
                                k + z1
                        );
                    } catch (ArithmeticException exception) {
                        result[i][j][k] = null;
                    }
                }
            }
        }

        return result;
    }

    private ArithmeticParser<Double> getDoubleParser() {
        Arithmetic<Double> arithmetic = new Arithmetic<>(
                Double::sum,
                (a, b) -> a - b,
                (a, b) -> a * b,
                (a, b) -> a / b,
                (a) -> -a
        );

        return new ArithmeticParser<>(
                arithmetic,
                Variable::new,
                Const::new,
                Double::parseDouble
        );
    }

    private ArithmeticParser<BigInteger> getBigIntegerParser() {
        Arithmetic<BigInteger> arithmetic = new Arithmetic<>(
                BigInteger::add,
                BigInteger::subtract,
                BigInteger::multiply,
                BigInteger::divide,
                BigInteger::negate
        );

        return new ArithmeticParser<>(
                arithmetic,
                Variable::new,
                Const::new,
                BigInteger::new
        );
    }

    private ArithmeticParser<Integer> getCheckedIntegerParser() {
        Arithmetic<Integer> arithmetic = new CheckedIntegerArithmetic();

        return new ArithmeticParser<>(
                arithmetic,
                Variable::new,
                Const::new,
                Integer::parseInt
        );
    }
}
