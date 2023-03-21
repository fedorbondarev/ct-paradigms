package expression.parser;

public class BaseParser {
    private static final char END = '\0';
    private final CharSource charSource;
    private char ch = 0;

    public BaseParser(final CharSource charSource) {
        this.charSource = charSource;
        take();
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected char take() {
        final char result = ch;
        ch = charSource.hasNext() ? charSource.next() : END;
        return result;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected IllegalArgumentException error(final String message) {
        return charSource.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    protected boolean eof() {
        return test(END);
    }

    protected void expect(final char expected) {
        if (!test(expected)) {
            throw error("Invalid char, expected " + expected);
        }
    }
}
