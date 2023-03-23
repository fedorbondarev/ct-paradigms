package expression.generic;

import expression.Priority;

public abstract class Multiply<T> extends BinaryExpression<T> {

    public Multiply(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public int getPriority() {
        return Priority.MULTIPLY;
    }

    @Override
    protected String getSymbol() {
        return "*";
    }

    @Override
    protected int getRightBracketsSufficientPriority() {
        return Priority.MULTIPLY;
    }

    @Override
    protected int getLeftBracketsSufficientPriority() {
        return Priority.DIVIDE;
    }
}
