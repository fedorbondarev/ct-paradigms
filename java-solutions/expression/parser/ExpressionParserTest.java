package expression.parser;

import expression.Expression;

public class ExpressionParserTest {
    public static void main(String[] args) {
        ExpressionParser expressionParser = new ExpressionParser(new StringSource("3 -    -    -      - x*3*6/2+0/4"));
        Expression parsedExpression = expressionParser.parse();
        System.out.println(parsedExpression.toMiniString());
        System.out.println(parsedExpression);
        System.out.println(parsedExpression.evaluate(5));
    }
}
