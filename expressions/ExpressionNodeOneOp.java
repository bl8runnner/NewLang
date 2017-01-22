package expressions;

import java.util.Iterator;

import exception.ParserException;
import node.Node;

public abstract class ExpressionNodeOneOp extends Node implements ExpressionType
{
    private Node myChild = null;

    public final Node child()
    {
        return this.myChild;
    }

    public final Node child(final Node child)
    {
        this.myChild = child;
        return child;
    }

    protected ExpressionNodeOneOp(final Node child, final String identifier) throws Exception
    {
        super(null, identifier);

        if(child == null)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_OPERATOR);
        }
        else
        {
            this.child(child);
        }
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

    public boolean remove(final Node node)
    {
        if(this.child() == node)
        {
            this.child(null);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    protected Node add(final Node node)
    {
        return this.child(node);
    }

    @Override
    public int size()
    {
        return 1;
    }

    @Override
    public void clear()
    {
        this.child(null);
    }

    public Node at(final int index)
    {
        if(index == 1)
        {
            return this.child();
        }
        else
        {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Iterator<Node> iterator()
    {
        return (new Iterator<Node>()
            {
                private final static int    NONE   = 0;

                private final static int    CHILD  = 1;

                private int                 myMode = CHILD;

                private ExpressionNodeOneOp myNode = null;

                @Override
                public boolean hasNext()
                {
                    return this.myMode != NONE;
                }

                @Override
                public Node next()
                {
                    switch(this.myMode)
                    {
                        case NONE:
                            return null;

                        case CHILD:
                            this.myMode = NONE;
                            return this.myNode.child();

                        default:
                            return null;

                    }
                }

                public Iterator<Node> init(final ExpressionNodeOneOp node)
                {
                    this.myMode = CHILD;
                    this.myNode = node;
                    return this;
                }

            }).init(this);
    }

}
