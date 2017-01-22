package expressions;

import exception.ParserException;
import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeCall extends ExpressionNodeTwoOps
{
    private final static String IDENTIFIER = "call()";

    public static ExpressionNodeCall create(final Expressions expressions) throws Exception
    {
        final Node right = expressions.popExpression();
        final Node left = expressions.popExpression();

        return create(left, right);
    }

    public static ExpressionNodeCall create(final Node left, final Node right) throws Exception
    {
        try
        {
            return new ExpressionNodeCall(left, right);
        }
        catch(ParserException exception)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_OPERANDS, IDENTIFIER, exception);
        }
    }

    private ExpressionNodeCall(final Node left, final Node right) throws Exception
    {
        super(left, right, IDENTIFIER);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
