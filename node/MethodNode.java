/**
 * 
 */
package node;

/**
 * @author LU132BOD
 *
 */
public final class MethodNode extends CompositeNodeAsVector
{
    /**
     * @param parent
     * @param identifier
     * @throws Exception
     */
    public MethodNode(final Node parent, final String identifier) throws Exception
    {
        super(parent, identifier);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

    @Override
    public Node at(int index)
    {
        return this.myContainer.elementAt(index);
    }

}
