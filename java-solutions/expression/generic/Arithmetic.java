package expression.generic;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Arithmetic<T extends Number> {
    private final BiFunction<T, T, T> add;
    private final BiFunction<T, T, T> subtract;
    private final BiFunction<T, T, T> multiply;
    private final BiFunction<T, T, T> divide;
    private final Function<T, T> negate;

    public Arithmetic(
            BiFunction<T, T, T> add,
            BiFunction<T, T, T> subtract,
            BiFunction<T, T, T> multiply,
            BiFunction<T, T, T> divide,
            Function<T, T> negate
    ) {
        this.add = add;
        this.subtract = subtract;
        this.multiply = multiply;
        this.divide = divide;
        this.negate = negate;
    }

    public Add<T> add(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        return new Add<>(leftExpression, rightExpression) {
            @Override
            protected T apply(T a, T b) {
                return add.apply(a, b);
            }
        };
    }

    public Subtract<T> subtract(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        return new Subtract<>(leftExpression, rightExpression) {
            @Override
            protected T apply(T a, T b) {
                return subtract.apply(a, b);
            }
        };
    }

    public Multiply<T> multiply(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        return new Multiply<>(leftExpression, rightExpression) {
            @Override
            protected T apply(T a, T b) {
                return multiply.apply(a, b);
            }
        };
    }

    public Divide<T> divide(ExpressionCommon<T> leftExpression, ExpressionCommon<T> rightExpression) {
        return new Divide<>(leftExpression, rightExpression) {
            @Override
            protected T apply(T a, T b) {
                return divide.apply(a, b);
            }
        };
    }

    public Negate<T> negate(ExpressionCommon<T> childExpression) {
        return new Negate<>(childExpression) {
            @Override
            protected T apply(T a) {
                return negate.apply(a);
            }
        };
    }
}
