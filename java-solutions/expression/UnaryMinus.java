package expression;

public class UnaryMinus extends UnaryExpression {
    public UnaryMinus(ExpressionCommon childExpression) {
        super(childExpression);
    }

    @Override
    public int getPriority() {
        return Priority.UNARY_MINUS;
    }

    @Override
    protected String getSymbol() {
        return "-";
    }

    @Override
    protected int getBracketsSufficientPriority() {
        return Priority.UNARY_MINUS;
    }

    @Override
    protected int apply(int a) {
        return -a;
    }
}
