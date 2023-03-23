package expression.generic;

public class CheckedIntegerArithmetic extends Arithmetic<Integer> {
    public CheckedIntegerArithmetic() {
        super(
                CheckedIntegerArithmetic::add,
                CheckedIntegerArithmetic::subtract,
                CheckedIntegerArithmetic::multiply,
                CheckedIntegerArithmetic::divide,
                CheckedIntegerArithmetic::negate
        );
    }

    protected static Integer add(Integer a, Integer b) {
        int r = a + b;

        if (((a ^ r) & (b ^ r)) < 0) {
            throw new ArithmeticException("int overflow");
        }

        return r;
    }

    protected static Integer subtract(Integer a, Integer b) {
        int r = a - b;

        if (((a ^ b) & (a ^ r)) < 0) {
            throw new ArithmeticException("int overflow");
        }

        return r;
    }

    protected static Integer multiply(Integer a, Integer b) {
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

    protected static Integer divide(Integer a, Integer b) {
        int r = a / b;

        if ((a & b & r) >= 0) {
            return r;
        }

        throw new ArithmeticException("int overflow");
    }

    protected static Integer negate(Integer a) {
        if (a == Integer.MIN_VALUE) {
            throw new ArithmeticException("int overflow");
        }

        return -a;
    }
}
