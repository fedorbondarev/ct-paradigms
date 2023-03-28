package expression.generic;

import java.math.BigInteger;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        class IntegerExpressionEvaluator extends ExpressionEvaluator<Integer, Integer> {
            public IntegerExpressionEvaluator(ExpressionCommon<Integer> expression) {
                super(expression);
            }

            @Override
            protected Integer getFrom(Integer a) {
                return a;
            }
        }

        var evaluator = switch (mode) {
            case "i" -> new IntegerExpressionEvaluator(getCheckedIntegerParser().parse(expression));

            case "d" -> new ExpressionEvaluator<Double, Integer>(getDoubleParser().parse(expression)) {
                @Override
                protected Double getFrom(Integer a) {
                    return (double) a;
                }
            };

            case "bi" -> new ExpressionEvaluator<BigInteger, Integer>(getBigIntegerParser().parse(expression)) {
                @Override
                protected BigInteger getFrom(Integer a) {
                    return BigInteger.valueOf(a);
                }
            };

            case "u" -> new IntegerExpressionEvaluator(getIntegerParser().parse(expression));

            case "f" -> new ExpressionEvaluator<Float, Integer>(getFloatParser().parse(expression)) {
                @Override
                protected Float getFrom(Integer a) {
                    return (float) a;
                }
            };

            case "s" -> new ExpressionEvaluator<Short, Integer>(getShortParser().parse(expression)) {
                @Override
                protected Short getFrom(Integer a) {
                    return a.shortValue();
                }
            };

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
        Arithmetic<Double> arithmetic = new Arithmetic<>() {
            @Override
            protected Double applyAdd(Double a, Double b) {
                return a + b;
            }

            @Override
            protected Double applySubtract(Double a, Double b) {
                return a - b;
            }

            @Override
            protected Double applyMultiply(Double a, Double b) {
                return a * b;
            }

            @Override
            protected Double applyDivide(Double a, Double b) {
                return a / b;
            }

            @Override
            protected Double applyNegate(Double a) {
                return -a;
            }
        };

        return new ArithmeticParser<>(arithmetic) {
            @Override
            protected Double parseConstValue(String numberString) {
                return Double.parseDouble(numberString);
            }
        };
    }

    private ArithmeticParser<BigInteger> getBigIntegerParser() {
        Arithmetic<BigInteger> arithmetic = new Arithmetic<>() {

            @Override
            protected BigInteger applyAdd(BigInteger a, BigInteger b) {
                return a.add(b);
            }

            @Override
            protected BigInteger applySubtract(BigInteger a, BigInteger b) {
                return a.subtract(b);
            }

            @Override
            protected BigInteger applyMultiply(BigInteger a, BigInteger b) {
                return a.multiply(b);
            }

            @Override
            protected BigInteger applyDivide(BigInteger a, BigInteger b) {
                return a.divide(b);
            }

            @Override
            protected BigInteger applyNegate(BigInteger a) {
                return a.negate();
            }
        };

        return new ArithmeticParser<>(arithmetic) {
            @Override
            protected BigInteger parseConstValue(String numberString) {
                return new BigInteger(numberString);
            }
        };
    }

    private ArithmeticParser<Integer> getCheckedIntegerParser() {
        Arithmetic<Integer> arithmetic = new CheckedIntegerArithmetic();

        return new ArithmeticParser<>(arithmetic) {
            @Override
            protected Integer parseConstValue(String numberString) {
                return Integer.parseInt(numberString);
            }
        };
    }

    private ArithmeticParser<Integer> getIntegerParser() {
        Arithmetic<Integer> arithmetic = new Arithmetic<>() {
            @Override
            protected Integer applyAdd(Integer a, Integer b) {
                return a + b;
            }

            @Override
            protected Integer applySubtract(Integer a, Integer b) {
                return a - b;
            }

            @Override
            protected Integer applyMultiply(Integer a, Integer b) {
                return a * b;
            }

            @Override
            protected Integer applyDivide(Integer a, Integer b) {
                return a / b;
            }

            @Override
            protected Integer applyNegate(Integer a) {
                return -a;
            }
        };

        return new ArithmeticParser<>(arithmetic) {
            @Override
            protected Integer parseConstValue(String numberString) {
                return Integer.parseInt(numberString);
            }
        };
    }

    private ArithmeticParser<Float> getFloatParser() {
        Arithmetic<Float> arithmetic = new Arithmetic<>() {
            @Override
            protected Float applyAdd(Float a, Float b) {
                return a + b;
            }

            @Override
            protected Float applySubtract(Float a, Float b) {
                return a - b;
            }

            @Override
            protected Float applyMultiply(Float a, Float b) {
                return a * b;
            }

            @Override
            protected Float applyDivide(Float a, Float b) {
                return a / b;
            }

            @Override
            protected Float applyNegate(Float a) {
                return -a;
            }
        };

        return new ArithmeticParser<>(arithmetic) {
            @Override
            protected Float parseConstValue(String numberString) {
                return Float.parseFloat(numberString);
            }
        };
    }

    private ArithmeticParser<Short> getShortParser() {
        Arithmetic<Short> arithmetic = new Arithmetic<>() {
            @Override
            protected Short applyAdd(Short a, Short b) {
                return (short) (a + b);
            }

            @Override
            protected Short applySubtract(Short a, Short b) {
                return (short) (a - b);
            }

            @Override
            protected Short applyMultiply(Short a, Short b) {
                return (short) (a * b);
            }

            @Override
            protected Short applyDivide(Short a, Short b) {
                return (short) (a / b);
            }

            @Override
            protected Short applyNegate(Short a) {
                return (short) -a;
            }
        };

        return new ArithmeticParser<>(arithmetic) {
            @Override
            protected Short parseConstValue(String numberString) {
                return Short.parseShort(numberString);
            }
        };
    }
}
