package expression.parser;

import expression.*;

public class ExpressionParser extends BaseParser implements TripleParser {
    protected IntExpressionCommon getAddExpression(IntExpressionCommon left, IntExpressionCommon right) {
        return new Add(left, right);
    }

    protected IntExpressionCommon getSubtractExpression(IntExpressionCommon left, IntExpressionCommon right) {
        return new Subtract(left, right);
    }

    protected IntExpressionCommon getMultiplyExpression(IntExpressionCommon left, IntExpressionCommon right) {
        return new Multiply(left, right);
    }

    protected IntExpressionCommon getDivideExpression(IntExpressionCommon left, IntExpressionCommon right) {
        return new Divide(left, right);
    }

    protected IntExpressionCommon getNegateExpression(IntExpressionCommon child) {
        return new Negate(child);
    }

    protected IntExpressionCommon getConstExpression(String constValue) {
        return new Const(Integer.parseInt(constValue));
    }

    protected IntExpressionCommon getVariableExpression(String variableName) {
        return new Variable(variableName);
    }

    public TripleExpression parse(String stringExpression) {
        init(new StringSource(stringExpression));

        IntExpressionCommon result = parseSum();

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

    private IntExpressionCommon parseSum() {
        skipWhitespaces();

        IntExpressionCommon term = parseTerm();

        while (true) {
            if (take('+')) {
                IntExpressionCommon secondTerm = parseTerm();
                term = getAddExpression(term, secondTerm);
            } else if (take('-')) {
                IntExpressionCommon secondTerm = parseTerm();
                term = getSubtractExpression(term, secondTerm);
            } else {
                break;
            }
        }

        skipWhitespaces();

        return term;
    }

    private IntExpressionCommon parseTerm() {
        skipWhitespaces();

        IntExpressionCommon factor = parseFactor();

        while (true) {
            if (take('*')) {
                IntExpressionCommon secondFactor = parseFactor();
                factor = getMultiplyExpression(factor, secondFactor);
            } else if (take('/')) {
                IntExpressionCommon secondFactor = parseFactor();
                factor = getDivideExpression(factor, secondFactor);
            } else {
                break;
            }
        }

        skipWhitespaces();

        return factor;
    }

    private IntExpressionCommon parseFactor() {
        skipWhitespaces();

        IntExpressionCommon result;

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

        skipWhitespaces();

        return sb.toString();
    }

    private IntExpressionCommon parseNumberOrVariable() {
        skipWhitespaces();

        IntExpressionCommon result;

        if (testAny('x', 'y', 'z')) {
            result = getVariableExpression(String.valueOf(take()));
        } else if (between('0', '9')) {
            try {
                result = getConstExpression(getNextNumbers());
            } catch (NumberFormatException exception) {
                throw error("Wrong number format", exception);
            }
        } else {
            throw error("Expected variable or number");
        }

        skipWhitespaces();

        return result;
    }

    private IntExpressionCommon parseUnaryMinus() {
        skipWhitespaces();

        take('-');

        if (between('0', '9')) {
            try {
                return getConstExpression("-" + getNextNumbers());
            } catch (NumberFormatException exception) {
                throw error("Wrong number format", exception);
            }
        }

        IntExpressionCommon result = getNegateExpression(parseFactor());

        skipWhitespaces();

        return result;
    }
}
