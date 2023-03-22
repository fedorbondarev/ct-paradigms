package expression.exceptions;

import expression.ExpressionCommon;

public class ExpressionParser extends expression.parser.ExpressionParser {
    @Override
    protected ExpressionCommon getAddExpression(ExpressionCommon left, ExpressionCommon right) {
        return new CheckedAdd(left, right);
    }

    @Override
    protected ExpressionCommon getDivideExpression(ExpressionCommon left, ExpressionCommon right) {
        return new CheckedDivide(left, right);
    }

    @Override
    protected ExpressionCommon getMultiplyExpression(ExpressionCommon left, ExpressionCommon right) {
        return new CheckedMultiply(left, right);
    }

    @Override
    protected ExpressionCommon getNegateExpression(ExpressionCommon child) {
        return new CheckedNegate(child);
    }

    @Override
    protected ExpressionCommon getSubtractExpression(ExpressionCommon left, ExpressionCommon right) {
        return new CheckedSubtract(left, right);
    }
}
