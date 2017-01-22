package expressions;

import exception.ParserException;
import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeDec extends ExpressionNodeOneOp
{
    private final static String IDENTIFIER = "--";

    public static ExpressionNodeDec create(final Expressions expressions) throws Exception
    {
        return create(expressions.popExpression());
    }

    public static ExpressionNodeDec create(final Node child) throws Exception
    {
        try
        {
            return new ExpressionNodeDec(child);
        }
        catch(ParserException exception)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_OPERANDS, IDENTIFIER, exception);
        }
    }

    private ExpressionNodeDec(final Node child) throws Exception
    {
        super(child, IDENTIFIER);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
