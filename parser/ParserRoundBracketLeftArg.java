package parser;

import expressions.Expressions;
import expressions.Expressions.Type;

public class ParserRoundBracketLeftArg extends ParserArg4ExpressionWithType
{
    private char myValidDelimeter = '\0';

    public ParserRoundBracketLeftArg(final char validDelimeter, final Type type, final Expressions expressions)
    {
        super(type, expressions);
        
        this.myValidDelimeter = validDelimeter;
    }

    public char getValidDelimeter()
    {
        return this.myValidDelimeter;
    }

}
