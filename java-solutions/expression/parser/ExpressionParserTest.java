package expression.parser;

import expression.ExpressionCommon;
import expression.TripleExpression;

public class ExpressionParserTest {
    public static void main(String[] args) {
        ExpressionParser expressionParser = new ExpressionParser();
        TripleExpression parsedExpression = expressionParser.parse("------ ---         ----   --------- x + 1");
        System.out.println(parsedExpression.toMiniString());
        System.out.println(parsedExpression);
        System.out.println(parsedExpression.evaluate(5, 0, 0));
    }
}
