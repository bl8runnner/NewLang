package expressions;

import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeBracketEdgeRight extends ExpressionNodeOneOp
{
    private final static String IDENTIFIER = "]";

    public static ExpressionNodeBracketEdgeRight create() throws Exception
    {
        return create(ExpressionNodeEmpty.create());
    }

    public static ExpressionNodeBracketEdgeRight create(final Expressions expressions) throws Exception
    {
        return create(expressions.popExpression());
    }

    public static ExpressionNodeBracketEdgeRight create(final Node child) throws Exception
    {
        return new ExpressionNodeBracketEdgeRight(child);
    }

    private ExpressionNodeBracketEdgeRight(final Node child) throws Exception
    {
        super((child == null) ? ExpressionNodeEmpty.create() : child, IDENTIFIER);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
