/**
 * 
 */
package node;

import java.util.Vector;

import modifier.Modifiers;

/**
 * @author LU132BOD
 *
 */
public final class ClassNode extends CompositeNodeAsVector
{
    private Vector<ClassNode> myDerivedFrom = new Vector<ClassNode>();
    
    private Modifiers         myModifiers   = null;
    
    public ClassNode(final Node parent, final String identifier, final Modifiers modifiers)
                    throws Exception
    {
        super(parent, identifier);
        
        this.modifiers(modifiers);
    }
    
    public ClassNode(final Node parent, final String identifier) throws Exception
    {
        this(parent, identifier, null);
    }
    
    public final void modifiers(final Modifiers modifiers)
    {
        this.myModifiers = modifiers;
    }
    
    public final Node derivedFrom(final ClassNode derivedFrom)
    {
        this.myDerivedFrom.addElement(derivedFrom);
        return derivedFrom;
    }
    
    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }
    
}
