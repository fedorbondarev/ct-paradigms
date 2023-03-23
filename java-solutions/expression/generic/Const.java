package expression.generic;

import expression.Priority;

import java.util.Objects;

public class Const<T> implements ExpressionCommon<T> {

    private final T value;

    public Const(T value) {
        this.value = value;
    }

    @Override
    public int getPriority() {
        return Priority.CONST;
    }

    @Override
    public T evaluate() {
        return value;
    }

    @Override
    public T evaluate(T x) {
        return value;
    }

    @Override
    public T evaluate(T x, T y) {
        return value;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Const<?> aConst = (Const<?>) o;
        return value.equals(aConst.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
