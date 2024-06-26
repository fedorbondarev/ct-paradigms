package expression;

public class Negate extends IntUnaryExpression {
    public Negate(IntExpressionCommon childExpression) {
        super(childExpression);
    }

    @Override
    public int getPriority() {
        return Priority.NEGATE;
    }

    @Override
    protected String getSymbol() {
        return "-";
    }

    @Override
    protected int getBracketsSufficientPriority() {
        return Priority.NEGATE;
    }

    @Override
    protected int apply(int a) {
        return -a;
    }
}
