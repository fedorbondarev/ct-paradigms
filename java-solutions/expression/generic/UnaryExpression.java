package expression.generic;

import java.util.Objects;

abstract public class UnaryExpression<T> implements ExpressionCommon<T> {
    private final ExpressionCommon<T> childExpression;

    public UnaryExpression(ExpressionCommon<T> childExpression) {
        this.childExpression = childExpression;
    }

    abstract protected String getSymbol();

    abstract protected int getBracketsSufficientPriority();

    abstract protected T apply(T a);

    @Override
    public T evaluate() {
        return apply(childExpression.evaluate());
    }

    @Override
    public T evaluate(T x) {
        return apply(childExpression.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y) {
        return apply(childExpression.evaluate(x, y));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return apply(childExpression.evaluate(x, y, z));
    }

    @Override
    public String toMiniString() {
        StringBuilder childString = new StringBuilder(childExpression.toMiniString());

        if (childExpression.getPriority() > getBracketsSufficientPriority()) {
            childString.insert(0, "(");
            childString.append(")");
        } else {
            childString.insert(0, " ");
        }

        return getSymbol() + childString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnaryExpression<?> that = (UnaryExpression<?>) o;
        return Objects.equals(childExpression, that.childExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(childExpression, getSymbol(), getPriority());
    }

    @Override
    public String toString() {
        return getSymbol() + "(" + childExpression + ")";
    }
}
