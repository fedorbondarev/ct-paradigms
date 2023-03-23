package expression.exceptions;

import expression.IntExpressionCommon;

public class ExpressionParser extends expression.parser.ExpressionParser {
    @Override
    protected IntExpressionCommon getAddExpression(IntExpressionCommon left, IntExpressionCommon right) {
        return new CheckedAdd(left, right);
    }

    @Override
    protected IntExpressionCommon getDivideExpression(IntExpressionCommon left, IntExpressionCommon right) {
        return new CheckedDivide(left, right);
    }

    @Override
    protected IntExpressionCommon getMultiplyExpression(IntExpressionCommon left, IntExpressionCommon right) {
        return new CheckedMultiply(left, right);
    }

    @Override
    protected IntExpressionCommon getNegateExpression(IntExpressionCommon child) {
        return new CheckedNegate(child);
    }

    @Override
    protected IntExpressionCommon getSubtractExpression(IntExpressionCommon left, IntExpressionCommon right) {
        return new CheckedSubtract(left, right);
    }
}
