package expressions;

import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeEmpty extends Node implements ExpressionType
{
    private final static String              IDENTIFIER = "EMPTY";

    private final static ExpressionNodeEmpty EMPTY      = ExpressionNodeEmpty.createEmpty();

    private static ExpressionNodeEmpty createEmpty()
    {
        try
        {
            return new ExpressionNodeEmpty();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    public final static ExpressionNodeEmpty create()
    {
        return EMPTY;
    }

    private ExpressionNodeEmpty() throws Exception
    {
        super(null, IDENTIFIER);

        this.newType(ExpressionType.Type.NONE);
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
