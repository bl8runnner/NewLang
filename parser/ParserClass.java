package parser;

import exception.ParserException;
import misc.SourceStream;
import modifier.Check4;
import modifier.Modifiers;
import node.CapitalIdentifierNode;
import node.ClassNode;
import node.NamespaceNode;
import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;
import node.PathNode;

public class ParserClass implements KeywordChecker
{
    private final static String KEYWORD             = "class";

    private final static String PATH_SEPARATOR      = new String(":");

    private final static String MINUS               = "-";

    private final static String GREATER             = ">";

    private final static String COMMA               = ",";

    private final static String CURLY_BRACKET_LEFT  = "{";

    private final static String CURLY_BRACKET_RIGHT = "}";

    private ClassNode handleDerived(final SourceStream stream, final Node parent) throws Exception
    {
        final PathNode pathNode = new PathNode();

        new CapitalIdentifierNode(pathNode, stream.check4Next());

        while(true)
        {
            String streamResult = stream.check4Next();

            if(streamResult.equals(PATH_SEPARATOR))
            {
                //
                // der pfad darf noch nicht leer sein
                //
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
                stream.toSnapshot();

                //
                // der Pfadname ist nicht vollständig, ergo wird nach oben
                // in dem baum nach einer Klasse gesucht
                //
                return (pathNode.size() == 1)
                        ? parent.traverseUp((new NodeDispatcher<ClassNode>()
                            {
                                private String myIdentifier = null;

                                public NodeDispatcherResult<ClassNode> visit(final ClassNode node)
                                        throws Exception
                                {
                                    return (node.identifier().equals(this.myIdentifier)) ? new NodeDispatcherResult<ClassNode>(node) : null;
                                }

                                public NodeDispatcher<ClassNode> init(final String identifier)
                                {
                                    this.myIdentifier = identifier;
                                    return this;
                                }
                            }).init(pathNode.first())).value()
                        : null;
            }
        }
    }

    public boolean parse(final SourceStream stream, final Node parent) throws Exception
    {
        stream.consumeBlancs();

        //
        // danach sollte 'class' kommen
        //
        if(!this.isKeyword(stream.check4Next()))
        {
            stream.toSnapshot();
            return false;
        }
        else
        {
            //
            // gibt es irgendwelche Modifizierer ?
            //
            Modifiers classModifiers = ParserModifier.getInstance().parse(stream);

            if(classModifiers.hasModifiers())
            {
                classModifiers.check(Check4.CLASSES);
            }
            else
            {
                classModifiers.addModifier(Modifiers.ModifierType.PUBLIC.identifier());
            }

            stream.toSnapshot();
            stream.consumeBlancs();

            final CapitalIdentifierNode capitalIdentifierNode = new CapitalIdentifierNode(stream.check4Next());

            NodeDispatcherResult<ClassNode> nodeDispatcherResult = parent.accept((new NodeDispatcher<ClassNode>()
                {
                    private CapitalIdentifierNode myCapitalIdentifierNode = null;

                    public NodeDispatcherResult<ClassNode> visit(final ClassNode node) throws Exception
                    {
                        return (this.myCapitalIdentifierNode.identifier().equals(node.identifier())) ? new NodeDispatcherResult<ClassNode>(node) : null;
                    }

                    public NodeDispatcher<ClassNode> init(final CapitalIdentifierNode capitalIdentifierNode)
                    {
                        this.myCapitalIdentifierNode = capitalIdentifierNode;
                        return this;
                    }

                }).init(capitalIdentifierNode));

            final ClassNode classNode = (nodeDispatcherResult != null) ? nodeDispatcherResult.value() : new ClassNode(parent, new CapitalIdentifierNode(stream.check4Next()).identifier(), classModifiers);

            stream.consumeBlancs();

            if(stream.check4Next().equals(MINUS))
            {
                //
                // ist es eine Subklasse von Superklassen vererbt ?
                //
                if(!stream.check4Next().equals(GREATER))
                {
                    throw new ParserException(
                            ParserException.ExceptionType.CLASS_DERIVED_OP_EXPECTED);
                }
                else
                {
                    while(true)
                    {
                        stream.consumeBlancs();
                        this.handleDerived(stream, parent);
                        stream.consumeBlancs();

                        if(!stream.check4Next().equals(COMMA))
                            break;
                    }
                }
            }
            else
            {
                stream.toSnapshot();
            }

            //
            // danach kommt der klassenkörper in {}
            //
            stream.consumeBlancs();

            if(!stream.check4Next().equals(CURLY_BRACKET_LEFT))
            {
                throw new ParserException(ParserException.ExceptionType.CLASS_NO_BRACKET_LEFT);
            }
            else
            {
                //
                // etwas anderes als klassen sind hier nicht erlaubt
                //
                // ParserClass.getInstance().parse(stream, namespaceNode);
                stream.consumeBlancs();

                if(!stream.check4Next().equals(CURLY_BRACKET_RIGHT))
                {
                    throw new ParserException(ParserException.ExceptionType.CLASS_NO_BRACKET_RIGHT);
                }
                else
                {
                    return true;
                }
            }
        }
    }

    private static ParserClass instance = null;

    public static ParserClass getInstance()
    {
        if(instance == null)
            instance = new ParserClass();

        return instance;
    }

    private ParserClass()
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
