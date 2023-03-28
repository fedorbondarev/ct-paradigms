package expression.generic;

import expression.parser.BaseParser;
import expression.parser.StringSource;

abstract public class ExpressionParser<T extends Number> extends BaseParser {

    protected abstract ExpressionCommon<T> getAddExpression(ExpressionCommon<T> left, ExpressionCommon<T> right);

    protected abstract ExpressionCommon<T> getSubtractExpression(ExpressionCommon<T> left, ExpressionCommon<T> right);

    protected abstract ExpressionCommon<T> getMultiplyExpression(ExpressionCommon<T> left, ExpressionCommon<T> right);

    protected abstract ExpressionCommon<T> getDivideExpression(ExpressionCommon<T> left, ExpressionCommon<T> right);

    protected abstract ExpressionCommon<T> getNegateExpression(ExpressionCommon<T> child);

    protected abstract ExpressionCommon<T> getConstExpression(T constValue);

    protected abstract ExpressionCommon<T> getVariableExpression(String variableName);

    protected abstract T parseConstValue(String numberString);

    public ExpressionCommon<T> parse(String stringExpression) {
        init(new StringSource(stringExpression));

        ExpressionCommon<T> result = parseSum();

        if (eof()) {
            return result;
        }

        throw error("End of expression expected");
    }

    private boolean testNumberOrVariable() {
        return test('x') || test('y') || test('z') || between('0', '9');
    }

    private void skipWhitespaces() {
        while (true) {
            if (!takeWhitespace()) break;
        }
    }

    private ExpressionCommon<T> parseSum() {
        skipWhitespaces();

        ExpressionCommon<T> term = parseTerm();

        while (true) {
            if (take('+')) {
                ExpressionCommon<T> secondTerm = parseTerm();
                term = getAddExpression(term, secondTerm);
            } else if (take('-')) {
                ExpressionCommon<T> secondTerm = parseTerm();
                term = getSubtractExpression(term, secondTerm);
            } else {
                break;
            }
        }

        skipWhitespaces();

        return term;
    }

    private ExpressionCommon<T> parseTerm() {
        skipWhitespaces();

        ExpressionCommon<T> factor = parseFactor();

        while (true) {
            if (take('*')) {
                ExpressionCommon<T> secondFactor = parseFactor();
                factor = getMultiplyExpression(factor, secondFactor);
            } else if (take('/')) {
                ExpressionCommon<T> secondFactor = parseFactor();
                factor = getDivideExpression(factor, secondFactor);
            } else {
                break;
            }
        }

        skipWhitespaces();

        return factor;
    }

    private ExpressionCommon<T> parseFactor() {
        skipWhitespaces();

        ExpressionCommon<T> result;

        if (take('(')) {
            result = parseSum();
            expect(')');
        } else if (testNumberOrVariable()) {
            result = parseNumberOrVariable();
        } else if (test('-')) {
            result = parseUnaryMinus();
        } else {
            throw error("Unexpected input, expected (, number, variable or unary minus");
        }

        skipWhitespaces();

        return result;
    }

    private String getNextNumbers() {
        skipWhitespaces();

        StringBuilder sb = new StringBuilder();
        while (between('0', '9')) {
            sb.append(take());
        }

        if (take('.')) {
            sb.append('.');
            while (between('0', '9')) {
                sb.append(take());
            }
        }

        skipWhitespaces();

        return sb.toString();
    }

    private ExpressionCommon<T> parseNumberOrVariable() {
        skipWhitespaces();

        ExpressionCommon<T> result;

        if (testAny('x', 'y', 'z')) {
            result = getVariableExpression(String.valueOf(take()));
        } else if (between('0', '9')) {
            try {
                result = getConstExpression(parseConstValue(getNextNumbers()));
            } catch (NumberFormatException exception) {
                throw error("Wrong number format", exception);
            }
        } else {
            throw error("Expected variable or number");
        }

        skipWhitespaces();

        return result;
    }

    private ExpressionCommon<T> parseUnaryMinus() {
        skipWhitespaces();

        take('-');

        if (between('0', '9')) {
            try {
                return getConstExpression(parseConstValue("-" + getNextNumbers()));
            } catch (NumberFormatException exception) {
                throw error("Wrong number format", exception);
            }
        }

        ExpressionCommon<T> result = getNegateExpression(parseFactor());

        skipWhitespaces();

        return result;
    }
}
