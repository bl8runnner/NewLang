package expressions;

import node.Node;

public abstract class ExpressionNodeComposite extends Node implements ExpressionType
{

    protected ExpressionNodeComposite(final String identifier) throws Exception
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
