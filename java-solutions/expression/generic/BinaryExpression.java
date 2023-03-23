package expression.generic;

import java.util.Objects;

public abstract class BinaryExpression<T> implements ExpressionCommon<T> {
    protected final ExpressionCommon<T> leftExpression;
    protected final ExpressionCommon<T> rightExpression;

    public BinaryExpression(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    abstract protected String getSymbol();

    abstract protected int getRightBracketsSufficientPriority();

    abstract protected int getLeftBracketsSufficientPriority();

    abstract protected T apply(T a, T b);


    @Override
    public T evaluate() {
        return apply(leftExpression.evaluate(), rightExpression.evaluate());
    }

    @Override
    public T evaluate(T x) {
        return apply(leftExpression.evaluate(x), rightExpression.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y) {
        return apply(leftExpression.evaluate(x, y), rightExpression.evaluate(x, y));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return apply(leftExpression.evaluate(x, y, z), rightExpression.evaluate(x, y, z));
    }

    @Override
    public String toMiniString() {
        StringBuilder leftPart = new StringBuilder(leftExpression.toMiniString());
        StringBuilder rightPart = new StringBuilder(rightExpression.toMiniString());

        if (leftExpression.getPriority() > getLeftBracketsSufficientPriority()) {
            leftPart.insert(0, "(");
            leftPart.append(")");
        }

        if (rightExpression.getPriority() > getRightBracketsSufficientPriority()) {
            rightPart.insert(0, "(");
            rightPart.append(")");
        }

        return leftPart + " " + getSymbol() + " " + rightPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryExpression<?> that = (BinaryExpression<?>) o;
        return Objects.equals(leftExpression, that.leftExpression) && Objects.equals(rightExpression, that.rightExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftExpression, rightExpression, getSymbol(), getPriority());
    }

    @Override
    public String toString() {
        return "(" + leftExpression + " " + getSymbol() + " " + rightExpression + ")";
    }
}
