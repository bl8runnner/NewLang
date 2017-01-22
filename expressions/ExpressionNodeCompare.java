package expressions;

import java.util.Iterator;

import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeCompare extends Node implements ExpressionType
{
    private final static String IDENTIFIER    = "COMPARE";

    private Node                myCondition   = null;

    private Node                myLessNode    = null;

    private Node                myIdentNode   = null;

    private Node                myGreaterNode = null;

    public static ExpressionNodeCompare create(final Node conditionNode, final Node lessNode, final Node identNode, final Node greaterNode) throws Exception
    {
        return new ExpressionNodeCompare(conditionNode, lessNode, identNode, greaterNode);
    }

    public final Node conditionNode()
    {
        return this.myCondition;
    }

    public final Node lessNode()
    {
        return this.myLessNode;
    }

    public final Node greaterNode()
    {
        return this.myGreaterNode;
    }

    public final Node identNode()
    {
        return this.myIdentNode;
    }

    public final Node conditionNode(final Node node)
    {
        this.myCondition = node;
        return node;
    }

    public final Node lessNode(final Node node)
    {
        this.myLessNode = node;
        return node;
    }

    public final Node greaterNode(final Node node)
    {
        this.myGreaterNode = node;
        return node;
    }

    public final Node identNode(final Node node)
    {
        this.myIdentNode = node;
        return node;
    }

    private ExpressionNodeCompare(final Node conditionNode, final Node lessNode, final Node identNode, final Node greaterNode) throws Exception
    {
        super(null, IDENTIFIER);

        this.conditionNode(conditionNode);
        this.lessNode(lessNode);
        this.greaterNode(greaterNode);
        this.identNode(identNode);
    }

    @Override
    protected Node add(final Node node)
    {
        if(this.conditionNode() == null)
        {
            this.conditionNode(node);
        }
        else
        {
            if(this.lessNode() == null)
            {
                this.lessNode(node);
            }
            else
            {
                if(this.greaterNode() == null)
                {
                    this.greaterNode(node);
                }
                else
                {
                    this.identNode(node);
                }
            }
        }

        return node;
    }

    @Override
    public int size()
    {
        return 4;
    }

    @Override
    public void clear()
    {
        this.conditionNode(null);
        this.lessNode(null);
        this.greaterNode(null);
        this.identNode(null);
    }

    @Override
    public Iterator<Node> iterator()
    {
        return (new Iterator<Node>()
            {
                private final static int      NONE      = 0;

                private final static int      CONDITION = 1;

                private final static int      LESS      = 2;

                private final static int      IDENT     = 3;

                private final static int      GREATER   = 4;

                private int                   myMode    = CONDITION;

                private ExpressionNodeCompare myNode    = null;

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

                        case CONDITION:
                            this.myMode = LESS;
                            return this.myNode.conditionNode();

                        case LESS:
                            this.myMode = IDENT;
                            return this.myNode.lessNode();

                        case IDENT:
                            this.myMode = GREATER;
                            return this.myNode.identNode();

                        case GREATER:
                            this.myMode = NONE;
                            return this.myNode.greaterNode();

                        default:
                            return null;

                    }
                }

                public Iterator<Node> init(final ExpressionNodeCompare node)
                {
                    this.myMode = CONDITION;
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

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        // TODO Auto-generated method stub
        return visitor.visit(this);
    }

    @Override
    public Node at(final int index)
    {
        switch(index)
        {
            case 1:
                return this.conditionNode();

            case 2:
                return this.lessNode();

            case 3:
                return this.identNode();

            case 4:
                return this.greaterNode();

            default:
                throw new IndexOutOfBoundsException();

        }
    }

    @Override
    public boolean remove(Node node)
    {
        if(this.conditionNode() == node)
        {
            this.conditionNode(null);
            return true;
        }
        else
        {
            if(this.lessNode() == node)
            {
                this.lessNode(null);
                return true;
            }
            else
            {
                if(this.identNode() == node)
                {
                    this.identNode(null);
                    return true;
                }
                else
                {
                    if(this.greaterNode() == node)
                    {
                        this.greaterNode(null);
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }
    }
}
