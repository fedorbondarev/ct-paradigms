package expression;

public class Multiply extends BinaryExpression {

    public Multiply(ExpressionCommon leftExpression, ExpressionCommon rightExpression) {
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
    protected int combineValues(int a, int b) {
        return a * b;
    }

    @Override
    protected int getRightBracketsSufficientPriority() {
        return Priority.MULTIPLY;
    }

    @Override
    protected int getLeftBracketsSufficientPriority() {
        return Priority.MULTIPLY;
    }
}
