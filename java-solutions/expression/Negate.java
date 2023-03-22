package expression;

public class Negate extends UnaryExpression {
    public Negate(ExpressionCommon childExpression) {
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
