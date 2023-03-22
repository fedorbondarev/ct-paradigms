package expression;

public interface ExpressionCommon extends Expression, TripleExpression {
    int getPriority();

    int evaluate();

    int evaluate(int x);

    int evaluate(int x, int y);

    int evaluate(int x, int y, int z);

    String toMiniString();
}
