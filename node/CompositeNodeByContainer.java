/**
 * 
 */
package node;

import java.util.AbstractCollection;
import java.util.Iterator;

import exception.ParserException;

/**
 * @author LU132BOD
 *
 */
public abstract class CompositeNodeByContainer<T extends AbstractCollection<Node>> extends Node
{
    protected T myContainer = null;

    /**
     * @param parent
     * @param identifier
     * @throws Exception 
     */
    protected CompositeNodeByContainer(final Node parent, final String identifier, final T container) throws Exception
    {
        super(parent, identifier);

        this.myContainer = container;
    }

    protected Node add(final Node node) throws ParserException
    {
        this.myContainer.add(node);
        return node;
    }
    
    public boolean remove(final Node node)
    {
        return this.myContainer.remove(node);
    }

    public int size()
    {
        return this.myContainer.size();
    }

    public void clear()
    {
        this.myContainer.clear();
    }

    public Iterator<Node> iterator()
    {
        return this.myContainer.iterator();
    }

}
