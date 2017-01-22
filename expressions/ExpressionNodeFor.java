package expressions;

import java.util.Iterator;

import exception.ParserException;
import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeFor extends Node implements ExpressionType
{
    private final static String IDENTIFIER = "FOR";

    public static ExpressionNodeFor create(final Node initNode, final Node conditionNode, final Node continueNode, final Node bodyNodeNode) throws Exception
    {
        return new ExpressionNodeFor(initNode, conditionNode, continueNode, bodyNodeNode);
    }

    private Node myInit      = null;

    private Node myCondition = null;

    private Node myContinue  = null;

    private Node myBody      = null;

    public final Node initNode()
    {
        return this.myInit;
    }

    public final Node conditionNode()
    {
        return this.myCondition;
    }

    public final Node continueNode()
    {
        return this.myContinue;
    }

    public final Node bodyNode()
    {
        return this.myBody;
    }

    public final Node initNode(final Node node)
    {
        this.myInit = node;
        return node;
    }

    public final Node conditionNode(final Node node)
    {
        this.myCondition = node;
        return node;
    }

    public final Node continueNode(final Node node)
    {
        this.myContinue = node;
        return node;
    }

    public final Node bodyNode(final Node node)
    {
        this.myBody = node;
        return node;
    }

    private ExpressionNodeFor(final Node initNode, final Node conditionNode, final Node continueNode, final Node bodyNodeNode) throws Exception
    {
        super(null, IDENTIFIER);

        if(bodyNodeNode == null)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_FOR_BODY);
        }
        else
        {
            this.initNode(initNode);
            this.conditionNode(conditionNode);
            this.continueNode(continueNode);
            this.bodyNode(bodyNodeNode);
        }
    }

    @Override
    protected Node add(final Node node)
    {
        if(this.initNode() == null)
        {
            this.initNode(node);
        }
        else
        {
            if(this.conditionNode() == null)
            {
                this.conditionNode(node);
            }
            else
            {
                if(this.continueNode() == null)
                {
                    this.continueNode(node);
                }
                else
                {
                    this.bodyNode(node);
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
        this.initNode(null);
        this.conditionNode(null);
        this.continueNode(null);
        this.bodyNode(null);
    }

    @Override
    public Iterator<Node> iterator()
    {
        return (new Iterator<Node>()
            {
                private final static int  NONE      = 0;

                private final static int  INIT      = 1;

                private final static int  CONDITION = 2;

                private final static int  CONTINUE  = 3;

                private final static int  BODY      = 4;

                private int               myMode    = INIT;

                private ExpressionNodeFor myNode    = null;

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

                        case INIT:
                            this.myMode = CONDITION;
                            return this.myNode.initNode();

                        case CONDITION:
                            this.myMode = CONTINUE;
                            return this.myNode.conditionNode();

                        case CONTINUE:
                            this.myMode = BODY;
                            return this.myNode.continueNode();

                        case BODY:
                            this.myMode = NONE;
                            return this.myNode.bodyNode();

                        default:
                            return null;

                    }
                }

                public Iterator<Node> init(final ExpressionNodeFor node)
                {
                    this.myMode = INIT;
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
                return this.initNode();

            case 2:
                return this.conditionNode();

            case 3:
                return this.continueNode();

            case 4:
                return this.bodyNode();

            default:
                throw new IndexOutOfBoundsException();

        }
    }

    @Override
    public boolean remove(Node node)
    {
        if(this.initNode() == node)
        {
            this.initNode(null);
            return true;
        }
        else
        {
            if(this.conditionNode() == node)
            {
                this.conditionNode(null);
                return true;
            }
            else
            {
                if(this.continueNode() == node)
                {
                    this.continueNode(null);
                    return true;
                }
                else
                {
                    if(this.bodyNode() == node)
                    {
                        this.bodyNode(null);
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