package expression.generic;

import expression.*;

abstract public class Add<T extends Number> extends BinaryExpression<T> {

    public Add(final ExpressionCommon<T> leftExpression, final ExpressionCommon<T> rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public int getPriority() {
        return Priority.ADD;
    }

    @Override
    protected String getSymbol() {
        return "+";
    }

    @Override
    protected int getRightBracketsSufficientPriority() {
        return Priority.ADD;
    }

    @Override
    protected int getLeftBracketsSufficientPriority() {
        return Priority.ADD;
    }
}
