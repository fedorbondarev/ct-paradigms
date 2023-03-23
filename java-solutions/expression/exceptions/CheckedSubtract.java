package expression.exceptions;

import expression.IntExpressionCommon;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(IntExpressionCommon leftExpression, IntExpressionCommon rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    protected int apply(int a, int b) {
        int r = a - b;

        if (((a ^ b) & (a ^ r)) < 0) {
            throw new ArithmeticException("int overflow");
        }

        return r;
    }
}
