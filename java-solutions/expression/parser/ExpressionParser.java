package expression.parser;

import expression.*;

public class ExpressionParser extends BaseParser implements TripleParser {
    protected ExpressionCommon getAddExpression(ExpressionCommon left, ExpressionCommon right) {
        return new Add(left, right);
    }

    protected ExpressionCommon getSubtractExpression(ExpressionCommon left, ExpressionCommon right) {
        return new Subtract(left, right);
    }

    protected ExpressionCommon getMultiplyExpression(ExpressionCommon left, ExpressionCommon right) {
        return new Multiply(left, right);
    }

    protected ExpressionCommon getDivideExpression(ExpressionCommon left, ExpressionCommon right) {
        return new Divide(left, right);
    }

    protected ExpressionCommon getNegateExpression(ExpressionCommon child) {
        return new Negate(child);
    }

    protected ExpressionCommon getConstExpression(int constValue) {
        return new Const(constValue);
    }

    protected ExpressionCommon getVariableExpression(String variableName) {
        return new Variable(variableName);
    }

    public TripleExpression parse(String stringExpression) {
        init(new StringSource(stringExpression));

        ExpressionCommon result = parseSum();

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

    private ExpressionCommon parseSum() {
        skipWhitespaces();

        ExpressionCommon term = parseTerm();

        while (true) {
            if (take('+')) {
                ExpressionCommon secondTerm = parseTerm();
                term = getAddExpression(term, secondTerm);
            } else if (take('-')) {
                ExpressionCommon secondTerm = parseTerm();
                term = getSubtractExpression(term, secondTerm);
            } else {
                break;
            }
        }

        skipWhitespaces();

        return term;
    }

    private ExpressionCommon parseTerm() {
        skipWhitespaces();

        ExpressionCommon factor = parseFactor();

        while (true) {
            if (take('*')) {
                ExpressionCommon secondFactor = parseFactor();
                factor = getMultiplyExpression(factor, secondFactor);
            } else if (take('/')) {
                ExpressionCommon secondFactor = parseFactor();
                factor = getDivideExpression(factor, secondFactor);
            } else {
                break;
            }
        }

        skipWhitespaces();

        return factor;
    }

    private ExpressionCommon parseFactor() {
        skipWhitespaces();

        ExpressionCommon result;

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

    private ExpressionCommon parseNumberOrVariable() {
        skipWhitespaces();

        ExpressionCommon result;

        if (testAny('x', 'y', 'z')) {
            result = getVariableExpression(String.valueOf(take()));
        } else if (between('0', '9')) {
            try {
                result = getConstExpression(Integer.parseInt(getNextNumbers()));
            } catch (NumberFormatException exception) {
                throw error("Wrong number format", exception);
            }
        } else {
            throw error("Expected variable or number");
        }

        skipWhitespaces();

        return result;
    }

    private ExpressionCommon parseUnaryMinus() {
        skipWhitespaces();

        take('-');

        if (between('0', '9')) {
            try {
                return getConstExpression(Integer.parseInt("-" + getNextNumbers()));
            } catch (NumberFormatException exception) {
                throw error("Wrong number format", exception);
            }
        }

        ExpressionCommon result = getNegateExpression(parseFactor());

        skipWhitespaces();

        return result;
    }
}
