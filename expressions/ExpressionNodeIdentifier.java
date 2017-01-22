package expressions;

import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeIdentifier extends Node implements ExpressionType
{
    public ExpressionNodeIdentifier(final String identifier) throws Exception
    {
        this(identifier, null);
    }

    public ExpressionNodeIdentifier(final String identifier, final ExpressionType.Type type) throws Exception
    {
        super(null, identifier);

        this.newType(type);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

    private ExpressionType.Type myType = null;

    @Override
    public ExpressionType.Type expressionType()
    {
        return this.myType;
    }

    @Override
    public void newType(final ExpressionType.Type type)
    {
        this.myType = type;
    }

}
