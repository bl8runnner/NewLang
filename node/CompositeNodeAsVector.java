package node;

import java.util.Vector;

public abstract class CompositeNodeAsVector extends CompositeNodeByContainer<Vector<Node>>
{
    protected CompositeNodeAsVector(final Node parent, final String identifier) throws Exception 
    {
        super(parent, identifier, new Vector<Node>(50));
    }

    public Node at(final int index) throws Exception
    {
        return this.myContainer.elementAt(index);
    }

}
