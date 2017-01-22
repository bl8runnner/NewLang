/**
 * 
 */
package node;

/**
 * @author LU132BOD
 *
 */
public class IdentifierNode extends Node
{

    /**
     * @throws Exception
     * 
     */
    public IdentifierNode(final Node parent, final String identifier) throws Exception
    {
        super(parent, identifier);
    }

    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
