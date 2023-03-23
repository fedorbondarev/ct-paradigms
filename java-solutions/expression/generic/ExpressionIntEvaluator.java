package expression.generic;

import java.util.function.Function;

public class ExpressionIntEvaluator<T> {
    final ExpressionCommon<T> expression;
    final Function<Integer, T> fromInt;

    public ExpressionIntEvaluator(ExpressionCommon<T> expression, Function<Integer, T> fromInt) {
        this.expression = expression;
        this.fromInt = fromInt;
    }

    public T evaluate(Integer x, Integer y, Integer z) {
        return expression.evaluate(fromInt.apply(x), fromInt.apply(y), fromInt.apply(z));
    }
}
