package expression.generic;

public abstract class Arithmetic<T extends Number> {
    abstract protected T applyAdd(T a, T b);

    abstract protected T applySubtract(T a, T b);

    abstract protected T applyMultiply(T a, T b);

    abstract protected T applyDivide(T a, T b);

    abstract protected T applyNegate(T a);

    public Add<T> getAdd(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        return new Add<>(leftExpression, rightExpression) {
            @Override
            protected T apply(T a, T b) {
                return applyAdd(a, b);
            }
        };
    }

    public Subtract<T> getSubtract(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        return new Subtract<>(leftExpression, rightExpression) {
            @Override
            protected T apply(T a, T b) {
                return applySubtract(a, b);
            }
        };
    }

    public Multiply<T> getMultiply(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        return new Multiply<>(leftExpression, rightExpression) {
            @Override
            protected T apply(T a, T b) {
                return applyMultiply(a, b);
            }
        };
    }

    public Divide<T> getDivide(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        return new Divide<>(leftExpression, rightExpression) {
            @Override
            protected T apply(T a, T b) {
                return applyDivide(a, b);
            }
        };
    }

    public Negate<T> getNegate(ExpressionCommon<T> childExpression) {
        return new Negate<>(childExpression) {
            @Override
            protected T apply(T a) {
                return applyNegate(a);
            }
        };
    }
}
