package expression;

public class Add extends BinaryExpression {

    public Add(final Expression leftExpression, final Expression rightExpression) {
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
    protected int combineValues(final int leftExpressionValue, final int rightExpressionValue) {
        return leftExpressionValue + rightExpressionValue;
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
