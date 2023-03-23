package expression;

public class Add extends IntBinaryExpression {

    public Add(final IntExpressionCommon leftExpression, final IntExpressionCommon rightExpression) {
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
    protected int apply(final int a, final int b) {
        return a + b;
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
