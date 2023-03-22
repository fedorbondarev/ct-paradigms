package expression;

public class Divide extends BinaryExpression {
    public Divide(ExpressionCommon leftExpression, ExpressionCommon rightExpression) {
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
    protected int apply(int a, int b) {
        return a / b;
    }

    @Override
    protected int getRightBracketsSufficientPriority() {
        return Priority.UNARY_MINUS;
    }

    @Override
    protected int getLeftBracketsSufficientPriority() {
        return Priority.DIVIDE;
    }
}
