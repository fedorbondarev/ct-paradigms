package expression.generic;

public abstract class ExpressionEvaluator<T, S> {
    final ExpressionCommon<T> expression;

    public ExpressionEvaluator(ExpressionCommon<T> expression) {
        this.expression = expression;
    }

    public T evaluate(S x, S y, S z) {
        return expression.evaluate(
                getFrom(x),
                getFrom(y),
                getFrom(z)
        );
    }

    abstract protected T getFrom(S a);
}
