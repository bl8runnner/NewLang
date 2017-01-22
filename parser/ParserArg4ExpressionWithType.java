package parser;

import expressions.Expressions;
import expressions.Expressions.Type;

public class ParserArg4ExpressionWithType
{
    private Type        myType        = null;

    private Expressions myExpressions = null;

    public ParserArg4ExpressionWithType(final Type type, final Expressions expressions)
    {
        this.myType = type;
        this.myExpressions = expressions;
    }

    public Type getType()
    {
        return this.myType;
    }

    public Expressions getExpressions()
    {
        return this.myExpressions;
    }

}
