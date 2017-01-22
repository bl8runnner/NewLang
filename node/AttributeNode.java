/**
 * 
 */
package node;

/**
 * @author LU132BOD
 *
 */
public class AttributeNode extends Node
{

    /**
     * @param parent
     * @param identifier
     * @throws Exception 
     */
    public AttributeNode(final Node parent, final String identifier) throws Exception
    {
        super(parent, identifier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Node.LeafNode#accept(Node.NodeVisitor)
     */
    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
