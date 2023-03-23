package expression;

import java.util.Objects;

abstract public class IntUnaryExpression implements IntExpressionCommon {
    private final IntExpressionCommon childExpression;

    public IntUnaryExpression(IntExpressionCommon childExpression) {
        this.childExpression = childExpression;
    }

    abstract protected String getSymbol();

    abstract protected int getBracketsSufficientPriority();

    abstract protected int apply(int a);

    @Override
    public int evaluate() {
        return apply(childExpression.evaluate());
    }

    @Override
    public int evaluate(int x) {
        return apply(childExpression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y) {
        return apply(childExpression.evaluate(x, y));
    }

    @Override
    public int evaluate(int x, int y, int z) {
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
        IntUnaryExpression that = (IntUnaryExpression) o;
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
