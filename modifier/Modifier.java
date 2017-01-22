/**
 * 
 */
package modifier;

/**
 * @author LU132BOD
 *
 */
public abstract class Modifier
{
    private String myIdentifier = null;
    
    protected Modifier(final String identifier)
    {
        this.myIdentifier = identifier;
    }
    
    public final String identifier()
    {
        return this.myIdentifier;
    }
    
    public abstract void check(final Check4 check4) throws Exception;
    
}
