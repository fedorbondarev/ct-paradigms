package expression.parser;

import expression.ExpressionCommon;

public class ExpressionParserTest {
    public static void main(String[] args) {
        ExpressionParser expressionParser = new ExpressionParser(new StringSource("3 -    -    -      - x*3*6/2+0/4"));
        ExpressionCommon parsedExpression = expressionParser.parse();
        System.out.println(parsedExpression.toMiniString());
        System.out.println(parsedExpression);
        System.out.println(parsedExpression.evaluate(5));
    }
}
