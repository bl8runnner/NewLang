package expressions;

import java.util.Iterator;

import exception.ParserException;
import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeIf extends Node implements ExpressionType
{
    private final static String IDENTIFIER = "IF";

    public static ExpressionNodeIf create(final Node conditionNode, final Node thenNode, final Node elseNode) throws Exception
    {
        return new ExpressionNodeIf(conditionNode, thenNode, elseNode);
    }

    private Node myConditionNode = null;

    private Node myThenNode      = null;

    private Node myElseNode      = null;

    public final Node conditionNode()
    {
        return this.myConditionNode;
    }

    public final Node thenNode()
    {
        return this.myThenNode;
    }

    public final Node elseNode()
    {
        return this.myElseNode;
    }

    public final Node conditionNode(final Node node)
    {
        this.myConditionNode = node;
        return node;
    }

    public final Node thenNode(final Node node)
    {
        this.myThenNode = node;
        return node;
    }

    public final Node elseNode(final Node node)
    {
        this.myElseNode = node;
        return node;
    }

    private ExpressionNodeIf(final Node conditionNode, final Node thenNode, final Node elseNode) throws Exception
    {
        super(null, IDENTIFIER);

        if(conditionNode == null)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_CONDITION_OPERATOR);
        }
        else
        {
            if(thenNode == null)
            {
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_THEN_OPERATOR);
            }
            else
            {
                this.conditionNode(conditionNode);
                this.thenNode(thenNode);
                this.elseNode(elseNode);
            }
        }
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
            if(this.thenNode() == null)
            {
                this.thenNode(node);
            }
            else
            {
                this.elseNode(node);
            }
        }

        return node;
    }

    @Override
    public int size()
    {
        return 3;
    }

    @Override
    public void clear()
    {
        this.conditionNode(null);
        this.thenNode(null);
        this.elseNode(null);
    }

    @Override
    public Iterator<Node> iterator()
    {
        return (new Iterator<Node>()
            {
                private final static int NONE      = 0;

                private final static int CONDITION = 1;

                private final static int THEN      = 2;

                private final static int ELSE      = 3;

                private int              myMode    = CONDITION;

                private ExpressionNodeIf myNode    = null;

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
                            this.myMode = THEN;
                            return this.myNode.conditionNode();

                        case THEN:
                            this.myMode = (this.myNode.elseNode() != null) ? ELSE : NONE;
                            return this.myNode.thenNode();

                        case ELSE:
                            this.myMode = NONE;
                            return this.myNode.elseNode();

                        default:
                            return null;

                    }
                }

                public Iterator<Node> init(final ExpressionNodeIf node)
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
                return this.thenNode();

            case 3:
                return this.elseNode();

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
            if(this.thenNode() == node)
            {
                this.thenNode(null);
                return true;
            }
            else
            {
                if(this.elseNode() == node)
                {
                    this.elseNode(null);
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
