package expression.generic;

public abstract class ArithmeticParser<T extends Number> extends ExpressionParser<T> {
    private final Arithmetic<T> arithmetic;

    public ArithmeticParser(Arithmetic<T> arithmetic) {
        this.arithmetic = arithmetic;
    }

    @Override
    protected ExpressionCommon<T> getAddExpression(ExpressionCommon<T> left, ExpressionCommon<T> right) {
        return arithmetic.getAdd(left, right);
    }

    @Override
    protected ExpressionCommon<T> getSubtractExpression(ExpressionCommon<T> left, ExpressionCommon<T> right) {
        return arithmetic.getSubtract(left, right);
    }

    @Override
    protected ExpressionCommon<T> getMultiplyExpression(ExpressionCommon<T> left, ExpressionCommon<T> right) {
        return arithmetic.getMultiply(left, right);

    }

    @Override
    protected ExpressionCommon<T> getDivideExpression(ExpressionCommon<T> left, ExpressionCommon<T> right) {
        return arithmetic.getDivide(left, right);
    }

    @Override
    protected ExpressionCommon<T> getNegateExpression(ExpressionCommon<T> child) {
        return arithmetic.getNegate(child);
    }

    @Override
    protected ExpressionCommon<T> getConstExpression(T constValue) {
        return new Const<>(constValue);
    }

    @Override
    protected ExpressionCommon<T> getVariableExpression(String variableName) {
        return new Variable<>(variableName);
    }

    @Override
    abstract protected T parseConstValue(String numberString);
}
