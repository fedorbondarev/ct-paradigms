package expression.parser;

public class StringSource implements CharSource {
    String str;
    int pos = 0;

    public StringSource(String str) {
        this.str = str;
    }

    public boolean hasNext() {
        return pos < str.length();
    }

    public char next() {
        return str.charAt(pos++);
    }

    @Override
    public IllegalArgumentException error(String message) {
        return new IllegalArgumentException(pos + " : " + message);
    }
}
