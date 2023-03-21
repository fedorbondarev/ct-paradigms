package expression.parser;

import expression.*;

public class ExpressionParser extends BaseParser {

    public ExpressionParser(final CharSource charSource) {
        super(charSource);
    }

    public Expression parse() {
        Expression result = nextExpression();
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

    private Expression nextExpression() {
        skipWhitespaces();

        boolean hasBrackets = take('(');

        Expression term = nextTerm();

        while (true) {
            if (take('+')) {
                Expression secondTerm = nextTerm();
                term = new Add(term, secondTerm);
            } else if (take('-')) {
                Expression secondTerm = nextTerm();
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

    private Expression nextTerm() {
        skipWhitespaces();

        Expression factor = nextFactor();

        while (true) {
            if (take('*')) {
                Expression secondFactor = nextFactor();
                factor = new Multiply(factor, secondFactor);
            } else if (take('/')) {
                Expression secondFactor = nextFactor();
                factor = new Divide(factor, secondFactor);
            } else {
                break;
            }
        }

        skipWhitespaces();

        return factor;
    }

    private Expression nextFactor() {
        skipWhitespaces();

        if (test('(')) {
            return nextExpression();
        } else if (test('x') || test('y') || test('z') || between('0', '9')) {
            return nextNumberOrVariable();
        } else if (take('-')) {
            return new Multiply(new Const(-1), nextFactor());
        } else {
            throw error("Unexpected input");
        }
    }

    private Expression nextNumberOrVariable() {
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
