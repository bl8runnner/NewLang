package modifier;

import java.util.Vector;

import exception.ParserException;
import parser.KeywordChecker;

public class Modifiers implements KeywordChecker
{
    public enum ModifierType
    {
        ABSTRACT(Abstract.getInstance()),
        CONST(Const.getInstance()),
        FINAL(Final.getInstance()),
        PRIVATE(Private.getInstance()),
        PROTECTED(Protected.getInstance()),
        PUBLIC(Public.getInstance()),
        STATIC(Static.getInstance()),
        TRANSIENT(Transient.getInstance());
        
        private Modifier myModifier = null;
        
        ModifierType(final Modifier modifier)
        {
            this.myModifier = modifier;
        }
        
        public final String identifier()
        {
            return this.myModifier.identifier();
        }
        
        public final boolean isKeyword(final String identifier)
        {
            return this.identifier().equals(identifier);
        }
        
        final void check(final Check4 check4)
                        throws Exception
        {
            this.myModifier.check(check4);
        }
        
        final Modifier modifier()
        {
            return this.myModifier;
        }
    }
    
    private final static int       MAX_MODIFIERS = 5;
    
    protected Vector<ModifierType> myModifiers   = new Vector<ModifierType>(MAX_MODIFIERS);
    
    /**
     */
    public Modifiers()
    {
    }
    
    public final void addModifier(final String identifier) throws ParserException
    {
        for(ModifierType modifier : ModifierType.values())
        {
            if(modifier.isKeyword(identifier))
            {
                if(this.myModifiers.size() == MAX_MODIFIERS)
                    throw new ParserException(
                                    ParserException.ExceptionType.TOO_MUCH_MODIFIERS,
                                    modifier.identifier());
                else
                {
                    for(ModifierType theModifiers : this.myModifiers)
                    {
                        if(modifier == theModifiers)
                            throw new ParserException(
                                            ParserException.ExceptionType.DOUBLE_MODIFIER,
                                            modifier.identifier());
                    }
                    
                    this.myModifiers.add(modifier);
                    return;
                }
            }
        }
    }
    
    public void check(final Check4 check4) throws Exception
    {        
        for(ModifierType modifier : this.myModifiers)
        {
            modifier.check(check4);
        }
    }
    
    @Override
    public boolean isKeyword(final String identifier) throws Exception
    {
        for(ModifierType modifier : ModifierType.values())
        {
            if(modifier.isKeyword(identifier))
            {
                return true;
            }
        }
        
        return false;
    }
    
    public final boolean hasModifiers()
    {
        return this.myModifiers.size() > 0;
    }
    
}
