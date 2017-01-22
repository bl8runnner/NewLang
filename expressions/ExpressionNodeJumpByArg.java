package expressions;

import exception.ParserException;
import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeJumpByArg extends ExpressionNodeTwoOps
{
    private final static String IDENTIFIER = "[] -> {}";

    public static ExpressionNodeJumpByArg create(final Expressions expressions) throws Exception
    {
        Node right = expressions.popExpression();
        Node left = expressions.popExpression();

        return create(left, right);
    }

    public static ExpressionNodeJumpByArg create(final Node left, final Node right) throws Exception
    {
        try
        {
            return new ExpressionNodeJumpByArg(left, right);
        }
        catch(ParserException exception)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_OPERANDS, IDENTIFIER, exception);
        }
    }

    private ExpressionNodeJumpByArg(final Node left, final Node right) throws Exception
    {
        super(left, right, IDENTIFIER);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
