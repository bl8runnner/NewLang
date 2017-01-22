/**
 * 
 */
package node;

import java.util.Iterator;

/**
 * @author ralf
 *
 */
public class PathNode extends CompositeNodeAsVector
{
    private final static String IDENTIFIER = "PATH";
    
    public PathNode() throws Exception
    {
        super(null, IDENTIFIER);
        new CapitalIdentifierNode(this, NamespaceNode.ROOT_NAME);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see node.Node#accept(node.NodeDispatcher)
     */
    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }
    
    public boolean equals(final PathNode other) throws Exception
    {
        NodeDispatcher<Boolean> pathChecker = new NodeDispatcher<Boolean>()
            {
                private CapitalIdentifierNode first = null;
                
                public NodeDispatcherResult<Boolean> visit(final CapitalIdentifierNode node)
                                throws Exception
                {
                    if(this.first == null)
                    {
                        this.first = node;
                        return new NodeDispatcherResult<Boolean>(true);
                    }
                    else
                    {
                        if(this.first.identifier().equals(node.identifier()))
                        {
                            this.first = null;
                            return new NodeDispatcherResult<Boolean>(true);
                        }
                        else
                        {
                            return null;
                        }
                    }
                }
            };
        
        if(this.size() != other.size())
        {
            return false;
        }
        else
        {
            for(int childs = 0; childs < this.size(); ++childs)
                if(this.at(childs).accept(pathChecker) == null)
                {
                    return false;
                }
                else
                {
                    if(other.at(childs).accept(pathChecker) == null)
                    {
                        return false;
                    }
                }
            
            return true;
        }
    }
    
    public final String first() throws Exception
    {
        return this.hasChilds() ? this.at(0).identifier() : null;
    }
    
    public final PathNode next() throws Exception
    {
        if(this.hasChilds())
        {
            this.remove(this.at(0));
        }
        
        return this;
    }
    
    public final PathNode tail() throws Exception
    {
        PathNode result = new PathNode().next();
        
        for(Iterator<Node> iterator = this.iterator(); iterator.hasNext();)
        {
            result.append(iterator.next());
        }
        
        return result.next();
    }
}
