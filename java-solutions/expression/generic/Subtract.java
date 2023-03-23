package expression.generic;

import expression.Priority;

abstract public class Subtract<T> extends BinaryExpression<T> {
    public Subtract(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public int getPriority() {
        return Priority.ADD;
    }

    @Override
    protected String getSymbol() {
        return "-";
    }

    @Override
    protected int getRightBracketsSufficientPriority() {
        return Priority.DIVIDE;
    }

    @Override
    protected int getLeftBracketsSufficientPriority() {
        return Priority.ADD;
    }
}
