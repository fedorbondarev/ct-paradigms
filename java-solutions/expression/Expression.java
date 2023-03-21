package expression;

public interface Expression {
    int getPriority();

    int evaluate();

    int evaluate(int x);

    int evaluate(int x, int y);

    int evaluate(int x, int y, int z);

    String toMiniString();
}
