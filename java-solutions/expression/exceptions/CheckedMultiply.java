package expression.exceptions;

import expression.IntExpressionCommon;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(IntExpressionCommon leftExpression, IntExpressionCommon rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    protected int apply(int a, int b) {
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
}
