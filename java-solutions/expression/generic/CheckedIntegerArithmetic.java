package expression.generic;

public class CheckedIntegerArithmetic extends Arithmetic<Integer> {
    @Override
    protected Integer applyAdd(Integer a, Integer b) {
        int r = a + b;

        if (((a ^ r) & (b ^ r)) < 0) {
            throw new ArithmeticException("int overflow");
        }

        return r;
    }

    @Override
    protected Integer applySubtract(Integer a, Integer b) {
        int r = a - b;

        if (((a ^ b) & (a ^ r)) < 0) {
            throw new ArithmeticException("int overflow");
        }

        return r;
    }

    @Override
    protected Integer applyMultiply(Integer a, Integer b) {
        int r = a * b;

        if (
                ((b != 0) && (r / b != a))
                        || (a == Integer.MIN_VALUE && b == -1)
                        || (b == Integer.MIN_VALUE && a == -1)
        ) {
            throw new ArithmeticException("int overflow");
        }

        return r;
    }

    @Override
    protected Integer applyDivide(Integer a, Integer b) {
        int r = a / b;

        if ((a & b & r) >= 0) {
            return r;
        }

        throw new ArithmeticException("int overflow");
    }

    @Override
    protected Integer applyNegate(Integer a) {
        if (a == Integer.MIN_VALUE) {
            throw new ArithmeticException("int overflow");
        }

        return -a;
    }
}
