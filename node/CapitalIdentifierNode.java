/**
 * 
 */
package node;

import exception.ParserException;

/**
 * @author LU132BOD
 *
 */
public class CapitalIdentifierNode extends IdentifierNode
{
    private static String checkCapital(final String identifier) throws Exception
    {
        final char theFirst = identifier.charAt(0);
        
        if((theFirst < 'A') || (theFirst > 'Z'))
        {
            throw new ParserException(
                            ParserException.ExceptionType.IDENTIFIER_NO_START_WITH_UPPERCASE);
        }
        else
        {
            return identifier;
        }
    }
    
    /**
     * @throws Exception
     * 
     */
    public CapitalIdentifierNode(final Node parent, final String identifier) throws Exception
    {
        super(parent, checkCapital(identifier));
    }
    
    public CapitalIdentifierNode(final String identifier) throws Exception
    {
        this(null, checkCapital(identifier));
    }
    
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }
    
}
