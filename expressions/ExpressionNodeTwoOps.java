package expressions;

import java.util.Iterator;

import exception.ParserException;
import node.Node;

public abstract class ExpressionNodeTwoOps extends Node implements ExpressionType
{
    private Node myLeft  = null;

    private Node myRight = null;

    public final Node left()
    {
        return this.myLeft;
    }

    public final Node right()
    {
        return this.myRight;
    }

    public final Node left(final Node node)
    {
        this.myLeft = node;
        return node;
    }

    public final Node right(final Node node)
    {
        this.myRight = node;
        return node;
    }

    protected ExpressionNodeTwoOps(final Node left, final Node right, final String identifier) throws Exception
    {
        super(null, identifier);

        if(left == null)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_LEFT_OPERATOR);
        }
        else
        {
            if(right == null)
            {
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_RIGHT_OPERATOR);
            }
            else
            {
                this.left(left);
                this.right(right);
            }
        }
    }

    public boolean remove(final Node node)
    {
        if(this.left() == node)
        {
            this.left(null);
            return true;
        }
        else
        {
            if(this.right() == node)
            {
                this.right(null);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    @Override
    protected Node add(final Node node)
    {
        return((this.left() == null) ? this.left(node) : ((this.right() == null) ? this.right(node) : null));
    }

    @Override
    public int size()
    {
        return 2;
    }

    @Override
    public void clear()
    {
        this.left(null);
        this.right(null);
    }

    public Node at(final int index)
    {
        switch(index)
        {
            case 1:
                return this.left();

            case 2:
                return this.right();

            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Iterator<Node> iterator()
    {
        return (new Iterator<Node>()
            {
                private final static int     NONE   = 0;

                private final static int     LEFT   = 1;

                private final static int     RIGHT  = 2;

                private int                  myMode = LEFT;

                private ExpressionNodeTwoOps myNode = null;

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

                        case LEFT:
                            this.myMode = RIGHT;
                            return this.myNode.left();

                        case RIGHT:
                            this.myMode = NONE;
                            return this.myNode.right();

                        default:
                            return null;

                    }
                }

                public Iterator<Node> init(final ExpressionNodeTwoOps node)
                {
                    this.myMode = LEFT;
                    this.myNode = node;
                    return this;
                }

            }).init(this);
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
