package expression.exceptions;

import expression.Divide;
import expression.ExpressionCommon;

public class CheckedDivide extends Divide {
    public CheckedDivide(ExpressionCommon leftExpression, ExpressionCommon rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    protected int apply(int a, int b) {
        int r = a / b;

        if ((a & b & r) >= 0) {
            return r;
        }

        throw new ArithmeticException("int overflow");
    }
}
