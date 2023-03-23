package expression.exceptions;

import expression.IntExpressionCommon;
import expression.Negate;

public class CheckedNegate extends Negate {
    public CheckedNegate(IntExpressionCommon childExpression) {
        super(childExpression);
    }

    @Override
    protected int apply(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new ArithmeticException("int overflow");
        }

        return -a;
    }
}
