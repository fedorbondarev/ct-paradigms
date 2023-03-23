package expression.generic;

public interface ExpressionCommon<T> {
    int getPriority();

    T evaluate();

    T evaluate(T x);

    T evaluate(T x, T y);

    T evaluate(T x, T y, T z);

    String toMiniString();
}
