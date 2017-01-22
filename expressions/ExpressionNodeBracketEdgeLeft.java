package expressions;

import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeBracketEdgeLeft extends ExpressionNodeMultiOps
{
    private final static String IDENTIFIER = "[";

    public static ExpressionNodeBracketEdgeLeft create() throws Exception
    {
        return new ExpressionNodeBracketEdgeLeft();
    }

    public static ExpressionNodeBracketEdgeLeft create(final Expressions expressions) throws Exception
    {
        return create();
    }

    private ExpressionNodeBracketEdgeLeft() throws Exception
    {
        super(IDENTIFIER);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
