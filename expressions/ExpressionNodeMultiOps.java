package expressions;

import node.CompositeNodeAsVector;

public abstract class ExpressionNodeMultiOps extends CompositeNodeAsVector implements ExpressionType
{
    protected ExpressionNodeMultiOps(final String identifier) throws Exception
    {
        super(null, identifier);
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
