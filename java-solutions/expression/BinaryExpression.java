package expression;

import java.util.Objects;

public abstract class BinaryExpression implements ExpressionCommon {

    protected ExpressionCommon leftExpression;
    protected ExpressionCommon rightExpression;

    public BinaryExpression(ExpressionCommon leftExpression, ExpressionCommon rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    abstract protected String getSymbol();

    abstract protected int getRightBracketsSufficientPriority();

    abstract protected int getLeftBracketsSufficientPriority();

    abstract protected int combineValues(int a, int b);


    @Override
    public int evaluate() {
        return combineValues(leftExpression.evaluate(), rightExpression.evaluate());
    }

    @Override
    public int evaluate(int x) {
        return combineValues(leftExpression.evaluate(x), rightExpression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y) {
        return combineValues(leftExpression.evaluate(x, y), rightExpression.evaluate(x, y));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return combineValues(leftExpression.evaluate(x, y, z), rightExpression.evaluate(x, y, z));
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
        BinaryExpression that = (BinaryExpression) o;
        return Objects.equals(leftExpression, that.leftExpression) && Objects.equals(rightExpression, that.rightExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftExpression, rightExpression);
    }

    @Override
    public String toString() {
        return "(" + leftExpression + " " + getSymbol() + " " + rightExpression + ")";
    }
}
