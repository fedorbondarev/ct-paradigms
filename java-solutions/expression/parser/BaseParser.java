package expression.parser;

public class BaseParser {
    private static final char END = '\0';
    private CharSource charSource = null;
    private char ch = 0;

    protected void init(final CharSource charSource) {
        this.charSource = charSource;
        take();
    }

    private void checkInit() {
        if (charSource == null) {
            throw new IllegalStateException("Parser is nor initialized");
        }
    }

    protected boolean test(final char expected) {
        checkInit();
        return ch == expected;
    }

    protected boolean testAny(final char... args) {
        for (char expected : args) {
            if (test(expected)) {
                return true;
            }
        }
        return false;
    }

    protected char take() {
        checkInit();
        final char result = ch;
        ch = charSource.hasNext() ? charSource.next() : END;
        return result;
    }

    protected boolean take(final char expected) {
        checkInit();
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean takeWhitespace() {
        checkInit();
        if (Character.isWhitespace(ch)) {
            take();
            return true;
        }
        return false;
    }

    protected IllegalArgumentException error(final String message) {
        return charSource.error(message);
    }

    protected IllegalArgumentException error(final String message, final Throwable cause) {
        IllegalArgumentException result = charSource.error(message);
        result.initCause(cause);
        return result;
    }

    protected boolean between(final char from, final char to) {
        checkInit();
        return from <= ch && ch <= to;
    }

    protected boolean eof() {
        return test(END);
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error("Invalid char, expected " + expected);
        }
    }
}
