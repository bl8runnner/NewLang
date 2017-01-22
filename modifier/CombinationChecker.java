/**
 * 
 */
package modifier;

import exception.ParserException;
import modifier.Modifiers.ModifierType;

/**
 * @author ralf
 *
 */
public interface CombinationChecker
{
    default public void
                    check(final ModifierType modifier, final ModifierType[] invalidCombinations)
                                    throws Exception
    {
        for(ModifierType invalid : invalidCombinations)
        {
            if(modifier == invalid)
            {
                throw new ParserException(
                                ParserException.ExceptionType.INVALID_MODIFIER_COMBINATION,
                                modifier.identifier());
            }
        }
    }
    
    default public void check(final Abstract modifier) throws ParserException, Exception
    {
        throw new ParserException(
                        ParserException.ExceptionType.INVALID_MODIFIER,
                        modifier.identifier());
    }
    
    default public void check(final Const modifier) throws ParserException
    {
        throw new ParserException(
                        ParserException.ExceptionType.INVALID_MODIFIER,
                        modifier.identifier());
    }
    
    default public void check(final Final modifier) throws ParserException, Exception
    {
        throw new ParserException(
                        ParserException.ExceptionType.INVALID_MODIFIER,
                        modifier.identifier());
    }
    
    default public void check(final Private modifier) throws ParserException, Exception
    {
        throw new ParserException(
                        ParserException.ExceptionType.INVALID_MODIFIER,
                        modifier.identifier());
    }
    
    default public void check(final Protected modifier) throws ParserException, Exception
    {
        throw new ParserException(
                        ParserException.ExceptionType.INVALID_MODIFIER,
                        modifier.identifier());
    }
    
    default public void check(final Public modifier) throws ParserException, Exception
    {
        throw new ParserException(
                        ParserException.ExceptionType.INVALID_MODIFIER,
                        modifier.identifier());
    }
    
    default public void check(final Static modifier) throws ParserException, Exception
    {
        throw new ParserException(
                        ParserException.ExceptionType.INVALID_MODIFIER,
                        modifier.identifier());
    }
    
    default public void check(final Transient modifier) throws ParserException, Exception
    {
        throw new ParserException(
                        ParserException.ExceptionType.INVALID_MODIFIER,
                        modifier.identifier());
    }
    
}
