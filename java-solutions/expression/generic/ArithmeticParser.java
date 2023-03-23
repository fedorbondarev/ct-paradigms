package expression.generic;

import java.util.function.Function;

public class ArithmeticParser<T extends Number> extends ExpressionParser<T> {
    private final Arithmetic<T> arithmetic;

    private final Function<String, ExpressionCommon<T>> variableParse;
    private final Function<T, ExpressionCommon<T>> constExpressionParse;
    private final Function<String, T> constElementParse;

    public ArithmeticParser(
            Arithmetic<T> arithmetic,
            Function<String, ExpressionCommon<T>> variableParse,
            Function<T, ExpressionCommon<T>> constParse,
            Function<String, T> numberParse
    ) {
        this.arithmetic = arithmetic;
        this.variableParse = variableParse;
        this.constExpressionParse = constParse;
        this.constElementParse = numberParse;
    }

    protected ExpressionCommon<T> getAddExpression(ExpressionCommon<T> left, ExpressionCommon<T> right) {
        return arithmetic.add(left, right);
    }

    protected ExpressionCommon<T> getSubtractExpression(ExpressionCommon<T> left, ExpressionCommon<T> right) {
        return arithmetic.subtract(left, right);
    }

    protected ExpressionCommon<T> getMultiplyExpression(ExpressionCommon<T> left, ExpressionCommon<T> right) {
        return arithmetic.multiply(left, right);

    }

    protected ExpressionCommon<T> getDivideExpression(ExpressionCommon<T> left, ExpressionCommon<T> right) {
        return arithmetic.divide(left, right);
    }

    protected ExpressionCommon<T> getNegateExpression(ExpressionCommon<T> child) {
        return arithmetic.negate(child);
    }

    protected ExpressionCommon<T> getConstExpression(T constValue) {
        return constExpressionParse.apply(constValue);
    }

    protected ExpressionCommon<T> getVariableExpression(String variableName) {
        return variableParse.apply(variableName);
    }

    @Override
    protected T getConstElement(String numberString) {
        return constElementParse.apply(numberString);
    }
}
