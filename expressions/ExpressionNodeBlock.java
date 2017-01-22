package expressions;

import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeBlock extends ExpressionNodeMultiOps
{
    private final static String IDENTIFIER = "{}";

    public static ExpressionNodeBlock create(final Expressions expressions) throws Exception
    {
        return new ExpressionNodeBlock(expressions);
    }

    private ExpressionNodeBlock(final Expressions expressions) throws Exception
    {
        super(IDENTIFIER);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
