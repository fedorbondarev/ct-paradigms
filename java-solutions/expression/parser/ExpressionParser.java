package expression.parser;

import expression.*;

public class ExpressionParser extends BaseParser {

    public ExpressionParser(final CharSource charSource) {
        super(charSource);
    }

    public Expression parse() {
        Expression result = parseSum();
        if (eof()) {
            return result;
        }
        throw error("End of expression expected");
    }

    private void skipWhitespaces() {
        while (true) {
            if (!take(' ')) break;
        }
    }

    private Expression parseSum() {
        skipWhitespaces();

        boolean hasBrackets = take('(');

        Expression term = parseTerm();

        while (true) {
            if (take('+')) {
                Expression secondTerm = parseTerm();
                term = new Add(term, secondTerm);
            } else if (take('-')) {
                Expression secondTerm = parseTerm();
                term = new Subtract(term, secondTerm);
            } else {
                break;
            }
        }


        if (hasBrackets) {
            expect(')');
            take(')');
        }

        skipWhitespaces();

        return term;
    }

    private Expression parseTerm() {
        skipWhitespaces();

        Expression factor = parseFactor();

        while (true) {
            if (take('*')) {
                Expression secondFactor = parseFactor();
                factor = new Multiply(factor, secondFactor);
            } else if (take('/')) {
                Expression secondFactor = parseFactor();
                factor = new Divide(factor, secondFactor);
            } else {
                break;
            }
        }

        skipWhitespaces();

        return factor;
    }

    private Expression parseFactor() {
        skipWhitespaces();

        if (test('(')) {
            return parseSum();
        } else if (test('x') || test('y') || test('z') || between('0', '9')) {
            return parseNumberOrVariable();
        } else if (take('-')) {
            return new Multiply(new Const(-1), parseFactor());
        } else {
            throw error("Unexpected input, expected (, number, variable or unary minus");
        }
    }

    private Expression parseNumberOrVariable() {
        if (take('x')) {
            return new Variable(Variable.VariableName.X);
        } else if (take('y')) {
            return new Variable(Variable.VariableName.Y);
        } else if (take('z')) {
            return new Variable(Variable.VariableName.Z);
        }

        StringBuilder sb = new StringBuilder();
        while (between('0', '9')) {
            sb.append(take());
        }
        return new Const(
                Integer.parseInt(
                        sb.toString()
                )
        );
    }
}
