package expression;

public class Multiply extends IntBinaryExpression {

    public Multiply(IntExpressionCommon leftExpression, IntExpressionCommon rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public int getPriority() {
        return Priority.MULTIPLY;
    }

    @Override
    protected String getSymbol() {
        return "*";
    }

    @Override
    protected int apply(int a, int b) {
        return a * b;
    }

    @Override
    protected int getRightBracketsSufficientPriority() {
        return Priority.MULTIPLY;
    }

    @Override
    protected int getLeftBracketsSufficientPriority() {
        return Priority.DIVIDE;
    }
}
