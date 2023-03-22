package expression;

public class Subtract extends BinaryExpression {

    public Subtract(ExpressionCommon leftExpression, ExpressionCommon rightExpression) {
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
    protected int combineValues(int a, int b) {
        return a - b;
    }

    @Override
    protected int getRightBracketsSufficientPriority() {
        return Priority.ADD;
    }

    @Override
    protected int getLeftBracketsSufficientPriority() {
        return Priority.MULTIPLY;
    }
}
