package expression.generic;

import expression.*;

public abstract class Divide<T> extends BinaryExpression<T> {
    public Divide(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public int getPriority() {
        return Priority.DIVIDE;
    }

    @Override
    protected String getSymbol() {
        return "/";
    }

    @Override
    protected int getRightBracketsSufficientPriority() {
        return Priority.NEGATE;
    }

    @Override
    protected int getLeftBracketsSufficientPriority() {
        return Priority.DIVIDE;
    }
}
