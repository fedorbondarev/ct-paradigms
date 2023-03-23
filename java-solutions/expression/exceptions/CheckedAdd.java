package expression.exceptions;

import expression.Add;
import expression.IntExpressionCommon;

public class CheckedAdd extends Add {
    public CheckedAdd(IntExpressionCommon leftExpression, IntExpressionCommon rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    protected int apply(int a, int b) {
        int r = a + b;

        if (((a ^ r) & (b ^ r)) < 0) {
            throw new ArithmeticException("int overflow");
        }

        return r;
    }
}
