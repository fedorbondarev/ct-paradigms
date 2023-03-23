package expression.generic;

import expression.Priority;

abstract public class Negate<T> extends UnaryExpression<T> {
    public Negate(ExpressionCommon<T> childExpression) {
        super(childExpression);
    }

    @Override
    public int getPriority() {
        return Priority.NEGATE;
    }

    @Override
    protected String getSymbol() {
        return "-";
    }

    @Override
    protected int getBracketsSufficientPriority() {
        return Priority.NEGATE;
    }
}
