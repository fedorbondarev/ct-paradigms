package expression;

public class Subtract extends IntBinaryExpression {

    public Subtract(IntExpressionCommon leftExpression, IntExpressionCommon rightExpression) {
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
    protected int apply(int a, int b) {
        return a - b;
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
