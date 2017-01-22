package expressions;

import java.util.Iterator;

import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeSwitch extends ExpressionNodeMultiOps implements ExpressionType
{
    private final static String IDENTIFIER      = "SWITCH";

    private Node                myConditionNode = null;

    public static ExpressionNodeSwitch create(final Expressions expressions) throws Exception
    {
        return new ExpressionNodeSwitch();
    }

    public final Node conditionNode()
    {
        return this.myConditionNode;
    }

    public final void conditionNode(final Node conditionNode)
    {
        this.myConditionNode = conditionNode;
    }

    private ExpressionNodeSwitch() throws Exception
    {
        super(IDENTIFIER);
    }

    @Override
    public int size()
    {
        return 1 + super.size();
    }

    @Override
    public void clear()
    {
        this.conditionNode(null);
        super.clear();
    }

    @Override
    public Iterator<Node> iterator()
    {
        return (new Iterator<Node>()
            {
                private final static int     NONE               = 0;

                private final static int     CONDITION          = 1;

                private final static int     OTHER              = 2;

                private int                  myMode             = CONDITION;

                private ExpressionNodeSwitch myNode             = null;

                private Iterator<Node>       myArgumentIterator = null;

                @Override
                public boolean hasNext()
                {
                    switch(this.myMode)
                    {
                        case NONE:
                            return false;

                        case CONDITION:
                            return true;

                        case OTHER:
                            return this.myArgumentIterator.hasNext();

                        default:
                            return false;

                    }
                }

                @Override
                public Node next()
                {
                    switch(this.myMode)
                    {
                        case NONE:
                            return null;

                        case CONDITION:
                            this.myMode = OTHER;
                            return this.myNode.conditionNode();

                        case OTHER:
                            this.myMode = this.myArgumentIterator.hasNext() ? OTHER : NONE;
                            return this.myArgumentIterator.next();

                        default:
                            return null;

                    }
                }

                public Iterator<Node> init(final ExpressionNodeSwitch node, final Iterator<Node> iterator)
                {
                    this.myArgumentIterator = iterator;
                    this.myMode = CONDITION;
                    this.myNode = node;
                    return this;
                }

            }).init(this, super.iterator());
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

    @Override
    public Node at(final int index)
    {
        return (index == 1) ? this.conditionNode() : this.myContainer.elementAt(index - 1);
    }

}
