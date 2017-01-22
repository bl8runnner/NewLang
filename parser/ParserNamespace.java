package parser;

import exception.ParserException;
import misc.SourceStream;
import node.CapitalIdentifierNode;
import node.NamespaceNode;
import node.Node;
import node.PathNode;

public class ParserNamespace implements KeywordChecker
{
    private final static String KEYWORD             = "namespace";
    
    private final static String PATH_SEPARATOR      = new String(":");
    
    private final static String CURLY_BRACKET_LEFT  = "{";
    
    private final static String CURLY_BRACKET_RIGHT = "}";
    
    private boolean parseBody(final SourceStream stream, final PathNode pathNode) throws Exception
    {
        while(true)
        {
            //
            // in einem namespace können klassen
            // aufgeführt sein
            //
            if(!ParserClass.getInstance().parse(stream, NamespaceNode.getRoot().getNamespaceNode(pathNode)))
            {
                stream.consumeBlancs();
                
                if(!stream.check4Next().equals(CURLY_BRACKET_RIGHT))
                {
                    throw new ParserException(
                                    ParserException.ExceptionType.NAMESPACE_NO_BRACKET_RIGHT);
                }
                else
                {
                    return true;
                }
            }
        }
    }
    
    public void parse(final SourceStream stream, final Node parent) throws Exception
    {
        while(true)
        {
            switch (stream.consumeBlancs())
            {
                case END_REACHED:
                    return;
                    
                default:
                    ;
            }
            
            //
            // danach sollte 'namespace' kommen
            //
            if(!this.isKeyword(stream.check4Next()))
            {
                throw new ParserException(ParserException.ExceptionType.NAMESPACE_EXPECTED);
            }
            else
            {
                stream.consumeBlancs();
                
                final PathNode pathNode = new PathNode();
                
                //
                // jetzt kommt der erste name.
                // evtl. kann der name durch :: getrennt
                // fortgeführt werden
                //
                new CapitalIdentifierNode(pathNode, stream.check4Next());
                
                while(true)
                {
                    stream.consumeBlancs();
                    
                    String identifier = stream.check4Next();
                    
                    if(identifier.equals(PATH_SEPARATOR))
                    {
                        if(pathNode.size() == 0)
                        {
                            throw new ParserException(ParserException.ExceptionType.EMPTY_PATH);
                        }
                        else
                        {
                            //
                            // der klassenpfad würde durch :: eingeleitet
                            //
                            if(stream.check4Next().equals(PATH_SEPARATOR))
                            {
                                new CapitalIdentifierNode(pathNode, stream.check4Next());
                            }
                            else
                            {
                                throw new ParserException(
                                                ParserException.ExceptionType.CLASS_WRONG_PATH_SEPARATOR);
                            }
                        }
                    }
                    else
                    {
                        //
                        // ab jetzt kommt der namespacekörper in {}
                        //
                        if(!identifier.equals(CURLY_BRACKET_LEFT))
                        {
                            throw new ParserException(
                                            ParserException.ExceptionType.NAMESPACE_NO_BRACKET_LEFT);
                        }
                        else
                        {
                            if(this.parseBody(stream, pathNode))
                            {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static ParserNamespace instance = null;
    
    public static ParserNamespace getInstance()
    {
        if(instance == null)
            instance = new ParserNamespace();
        
        return instance;
    }
    
    private ParserNamespace()
    {
    }
    
    @Override
    public boolean isKeyword(final String identifier)
    {
        return identifier.equals(KEYWORD);
    }
    
    public static void main(String[] args)
    {
        SourceStream stream = SourceStream.getInstance();
        
        stream.clear();
        stream.addString("namespace Bode {}");
        stream.addString("namespace Bode::Reinhard {}");
        stream.addString("namespace Bode::Susanne {}");
        stream.addString("namespace Bode::Reinhard::Ernst::Ralf");
        stream.addString("{");
        stream.addString("  class Hugo");
        stream.addString("  {");
        stream.addString("  }");
        stream.addString("  class private Kunigunde -> Ingeborg, Hugo");
        stream.addString("  {");
        stream.addString("  }");
        stream.addString("  class public abstract Ingeborg");
        stream.addString("  {");
        stream.addString("  }");
        stream.addString("}");

        stream.reset();
        
        try
        {
            ParserNamespace.getInstance().parse(stream, NamespaceNode.getRoot());
            NamespaceNode.getRoot().print(0);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
