package parser;

import misc.SourceStream;
import modifier.Modifiers;
import modifier.Modifiers.ModifierType;

public class ParserModifier implements KeywordChecker
{
    private static ParserModifier instance = null;

    public final static ParserModifier getInstance()
    {
        if(instance == null)
            instance = new ParserModifier();

        return instance;
    }

    private ParserModifier()
    {
    }

    public Modifiers parse(final SourceStream stream) throws Exception
    {
        Modifiers modifiers = new Modifiers();

        while(true)
        {
            stream.consumeBlancs();
            
            if(!this.isKeyword(stream.check4Next()))
            {
                stream.toSnapshot();
                return modifiers;
            }
            else
            {
                modifiers.addModifier(stream.repeat());
            }
        }
    }

    @Override
    public boolean isKeyword(final String identifier)
    {
        for(Modifiers.ModifierType modifier : ModifierType.values())
            if(modifier.isKeyword(identifier))
            {
                return true;
            }

        return false;
    }
}
