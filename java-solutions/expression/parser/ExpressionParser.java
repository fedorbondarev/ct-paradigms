package expression.parser;

import expression.*;

public class ExpressionParser extends BaseParser implements TripleParser {
    public TripleExpression parse(String stringExpression) {
        init(new StringSource(stringExpression));

        ExpressionCommon result = parseSum();

        if (eof()) {
            return result;
        }

        throw error("End of expression expected");
    }

    private void skipWhitespaces() {
        while (true) {
            if (!takeWhitespace()) {
                break;
            }
        }
    }

    private ExpressionCommon parseSum() {
        skipWhitespaces();

        ExpressionCommon term = parseTerm();

        while (true) {
            if (take('+')) {
                ExpressionCommon secondTerm = parseTerm();
                term = new Add(term, secondTerm);
            } else if (take('-')) {
                ExpressionCommon secondTerm = parseTerm();
                term = new Subtract(term, secondTerm);
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
                factor = new Multiply(factor, secondFactor);
            } else if (take('/')) {
                ExpressionCommon secondFactor = parseFactor();
                factor = new Divide(factor, secondFactor);
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

    private boolean testNumberOrVariable() {
        return test('x') || test('y') || test('z') || between('0', '9');
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

        if (take('x')) {
            result = new Variable(Variable.VariableName.X);
        } else if (take('y')) {
            result = new Variable(Variable.VariableName.Y);
        } else if (take('z')) {
            result = new Variable(Variable.VariableName.Z);
        } else if (between('0', '9')) {
            result = new Const(Integer.parseInt(
                    getNextNumbers()
            ));
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
            return new Const(
                    Integer.parseInt(
                            "-" + getNextNumbers()
                    )
            );
        }

        ExpressionCommon result = new UnaryMinus(parseFactor());

        skipWhitespaces();

        return result;
    }
}
