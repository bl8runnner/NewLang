/**
 * 
 */
package node;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

import exception.ParserException;
import misc.SourceStream;

/**
 * @author LU132BOD
 *
 */
public abstract class Node
{
    private Node   myParent     = null;

    private String myIdentifier = null;

    private int    myColumn     = -1;

    private int    myRow        = -1;

    public final Node parent()
    {
        return this.myParent;
    }

    protected void newParent(final Node parent) throws Exception
    {
        if(parent != null)
        {
            if(this.myParent != parent)
            {
                this.myParent = parent;
                parent.append(this);
            }
        }
    }

    public final String identifier()
    {
        return this.myIdentifier;
    }

    public final int column()
    {
        return this.myColumn;
    }

    public final int row()
    {
        return this.myRow;
    }

    protected Node(final Node parent, final String identifier) throws Exception
    {
        this.myIdentifier = identifier;
        this.myRow = SourceStream.getInstance().currentLine();
        this.myColumn = SourceStream.getInstance().currentColumn();
        this.newParent(parent);
    }

    @SuppressWarnings(
        {
                "unused"
        })
    private Node()
    {
    }

    protected Node add(final Node node) throws ParserException
    {
        return null;
    }

    public int size()
    {
        return 0;
    }

    public final boolean hasChilds()
    {
        return this.size() > 0;
    }

    public void clear()
    {
    }

    public Iterator<Node> iterator()
    {
        return new Iterator<Node>()
            {

                @Override
                public boolean hasNext()
                {
                    return false;
                }

                @Override
                public Node next()
                {
                    return null;
                }

            };
    }

    public <T> NodeDispatcherResult<T> forEachChild(
            final NodeDispatcher<T> nodeDispatcher,
            final Predicate<NodeDispatcherResult<T>> stopFunction,
            final Function<NodeDispatcherResult<T>, NodeDispatcherResult<T>> resultFunction,
            final Function<Node, NodeDispatcherResult<T>> defaultResultFunction) throws Exception
    {
        if(this.hasChilds())
        {
            for(Iterator<Node> iterator = this.iterator(); iterator.hasNext();)
            {
                NodeDispatcherResult<T> nodeDispatcherResult = iterator.next().accept(nodeDispatcher);

                if(stopFunction.test(nodeDispatcherResult))
                {
                    return resultFunction.apply(nodeDispatcherResult);
                }
            }
        }

        return defaultResultFunction.apply(this);
    }

    public final static Predicate<NodeDispatcherResult<?>>                         DEFAULT_STOP_FUNCTION           = (NodeDispatcherResult<?> nodeDispatcherResult) ->
                                                                                                                       {
                                                                                                                           return(nodeDispatcherResult != null);
                                                                                                                       };

    public final static Function<NodeDispatcherResult<?>, NodeDispatcherResult<?>> DEFAULT_RESULT_FUNCTION         = (NodeDispatcherResult<?> nodeDispatcherResult) ->
                                                                                                                       {
                                                                                                                           return nodeDispatcherResult;
                                                                                                                       };

    public final static Function<Node, NodeDispatcherResult<?>>                    DEFAULT_DEFAULT_RESULT_FUNCTION = (Node node) ->
                                                                                                                       {
                                                                                                                           return null;
                                                                                                                       };

    @SuppressWarnings("unchecked")
    public <T> NodeDispatcherResult<T> forEachChild(
            final NodeDispatcher<T> nodeDispatcher,
            final Predicate<NodeDispatcherResult<T>> stopFunction,
            final Function<NodeDispatcherResult<T>, NodeDispatcherResult<T>> resultFunction) throws Exception
    {
        if(this.hasChilds())
        {
            for(Iterator<Node> iterator = this.iterator(); iterator.hasNext();)
            {
                NodeDispatcherResult<T> nodeDispatcherResult = iterator.next().accept(nodeDispatcher);

                if(stopFunction.test(nodeDispatcherResult))
                {
                    return resultFunction.apply(nodeDispatcherResult);
                }
            }
        }

        return (NodeDispatcherResult<T>) DEFAULT_DEFAULT_RESULT_FUNCTION.apply(this);
    }

    @SuppressWarnings("unchecked")
    public <T> NodeDispatcherResult<T> forEachChild(
            final NodeDispatcher<T> nodeDispatcher,
            final Predicate<NodeDispatcherResult<T>> stopFunction) throws Exception
    {
        if(this.hasChilds())
        {
            for(Iterator<Node> iterator = this.iterator(); iterator.hasNext();)
            {
                NodeDispatcherResult<T> nodeDispatcherResult = iterator.next().accept(nodeDispatcher);

                if(stopFunction.test(nodeDispatcherResult))
                {
                    return (NodeDispatcherResult<T>) DEFAULT_RESULT_FUNCTION.apply(nodeDispatcherResult);
                }
            }
        }

        return (NodeDispatcherResult<T>) DEFAULT_DEFAULT_RESULT_FUNCTION.apply(this);
    }

    @SuppressWarnings("unchecked")
    public <T> NodeDispatcherResult<T> forEachChild(final NodeDispatcher<T> nodeDispatcher) throws Exception
    {
        if(this.hasChilds())
        {
            for(Iterator<Node> iterator = this.iterator(); iterator.hasNext();)
            {
                NodeDispatcherResult<T> nodeDispatcherResult = iterator.next().accept(nodeDispatcher);

                if(DEFAULT_STOP_FUNCTION.test(nodeDispatcherResult))
                {
                    return (NodeDispatcherResult<T>) DEFAULT_RESULT_FUNCTION.apply(nodeDispatcherResult);
                }
            }
        }

        return (NodeDispatcherResult<T>) DEFAULT_DEFAULT_RESULT_FUNCTION.apply(this);
    }

    public <T> NodeDispatcherResult<T> traverseUp(final NodeDispatcher<T> nodeDispatcher) throws Exception
    {
        NodeDispatcherResult<T> nodeDispatcherResult = this.forEachChild(nodeDispatcher);

        return (nodeDispatcherResult != null) ? nodeDispatcherResult : (this.parent() == null) ? null : this.parent().traverseUp(nodeDispatcher);
    }

    public Node append(final Node node) throws Exception
    {
        if(node != null)
        {
            if(this.hasChilds())
            {
                for(Iterator<Node> iterator = this.iterator(); iterator.hasNext();)
                {
                    if(iterator.next() == node)
                    {
                        return node;
                    }
                }
            }

            this.add(node);
            node.newParent(this);
        }

        return node;
    }

    public boolean remove(final Node node)
    {
        return false;
    }

    public Node at(final int index) throws Exception
    {
        return null;
    }

    private final static String   DELIM = " : ";

    protected final static String SPACE = "  ";

    public void print(final int indents)
    {
        for(int spaces = 0; spaces < indents; ++spaces)
            System.out.print(SPACE);

        System.out.print(this.getClass().getName());
        System.out.print(DELIM);
        System.out.println(this.identifier());

        for(Iterator<Node> iterator = this.iterator(); iterator.hasNext();)
        {
            iterator.next().print(indents + 1);
        }
    }

    public abstract <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception;

}
