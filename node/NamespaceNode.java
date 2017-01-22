/**
 * 
 */
package node;

import exception.ParserException;

/**
 * @author LU132BOD
 *
 */
public final class NamespaceNode extends CompositeNodeAsVector
{
    public final static String   ROOT_NAME  = "ROOT";

    public final static String   IDENTIFIER = "namespace";

    private static NamespaceNode ROOT       = null;

    public static NamespaceNode getRoot()
    {
        if(ROOT == null)
        {
            try
            {
                ROOT = new NamespaceNode();
            }
            catch(Exception exception)
            {
                // TODO Auto-generated catch block
                exception.printStackTrace();
                return null;
            }
        }

        return ROOT;
    }

    /**
     * @param parent
     * @param identifier
     * @throws Exception
     */
    public NamespaceNode(final Node parent, final String identifier) throws Exception
    {
        super(parent, identifier);
    }

    /**
     * @param identifier
     * @throws Exception
     */
    private NamespaceNode() throws Exception
    {
        super(null, ROOT_NAME);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

    public final NamespaceNode getNamespaceNode(final PathNode pathNode) throws Exception
    {
        if(pathNode.first() == null)
        {
            return null;
        }
        else
        {
            if(!this.identifier().equals(pathNode.first()))
            {
                //
                // dieses namespaceelement ist nicht im pfad aufgeführt
                //
                return null;
            }
            else
            {
                //
                // nun werden weitere pfadanteile berücksichtigt (sofern
                // vorhanden) und evtl. kindknoten unter dem aktuellen namespace
                //
                final PathNode tail = pathNode.tail();

                if(!tail.hasChilds())
                {
                    //
                    // der pfad wurde komplett abgearbeitet und der letzte
                    // eintrag im pfad war gleichbedeutend mit diesem
                    // aktuellen namespaceknoten
                    //
                    return this;
                }
                else
                {
                    final NodeDispatcherResult<NamespaceNode> nodeDispatcherResult = this.forEachChild((new NodeDispatcher<NamespaceNode>()
                        {
                            private PathNode myPathNode = null;

                            public NodeDispatcherResult<NamespaceNode> visit(
                                    final NamespaceNode node)
                                    throws Exception
                            {
                                final NamespaceNode namespaceNode = node.getNamespaceNode(myPathNode);

                                return (namespaceNode == null) ? null : new NodeDispatcherResult<NamespaceNode>(namespaceNode);
                            }

                            public NodeDispatcher<NamespaceNode> init(final PathNode pathNode)
                            {
                                this.myPathNode = pathNode;
                                return this;
                            }
                        }).init(tail));

                    //
                    // der erste eintrag des verkürzten pfads konnte nicht
                    // in den kindknoten ermittelt werden, also wird ein neuer
                    // namespaceknoten angelegt und das geraffel wird wieder
                    // rekursiv verarbeitet
                    //
                    return (nodeDispatcherResult != null) ? nodeDispatcherResult.value() : new NamespaceNode(this, tail.first()).getNamespaceNode(tail);
                }
            }
        }
    }

    public final ClassNode getClassNode(final PathNode pathNode) throws Exception
    {
        if(pathNode.first() == null)
        {
            return null;
        }
        else
        {
            if(!this.identifier().equals(pathNode.first()))
            {
                //
                // dieses namespaceelement ist nicht im pfad aufgeführt
                //
                return null;
            }
            else
            {
                //
                // nun werden weitere pfadanteile berücksichtigt (sofern
                // vorhanden) und evtl. kindknoten unter dem aktuellen namespace
                //
                final PathNode tail = pathNode.tail();

                if(!tail.hasChilds())
                {
                    //
                    // der pfad wurde komplett abgearbeitet und der letzte
                    // eintrag im pfad war gleichbedeutend mit diesem
                    // aktuellen namespaceknoten
                    // klassennamen dürfen nicht der abschluss eines
                    // namespace sein
                    //
                    throw new ParserException(
                            ParserException.ExceptionType.CLASSNAME2NAMESPACE,
                            this.identifier());
                }
                else
                {
                    final ClassNode classNode = this.forEachChild((new NodeDispatcher<ClassNode>()
                        {
                            private PathNode myPathNode = null;

                            public NodeDispatcherResult<ClassNode> visit(
                                    final NamespaceNode node)
                                    throws Exception
                            {
                                final ClassNode childNode = node.getClassNode(myPathNode);

                                return (childNode == null) ? null : new NodeDispatcherResult<ClassNode>(childNode);
                            }

                            public NodeDispatcherResult<ClassNode> visit(
                                    final ClassNode node)
                                    throws Exception
                            {
                                return (this.myPathNode.size() == 1)
                                        ? (this.myPathNode.first().equals(node.identifier()) ? new NodeDispatcherResult<ClassNode>(node) : null)
                                        : null;
                            }

                            public NodeDispatcher<ClassNode> init(final PathNode pathNode)
                            {
                                this.myPathNode = pathNode;
                                return this;
                            }
                        }).init(tail)).value();

                    if(classNode != null)
                    {
                        return classNode;
                    }
                }

                if(tail.size() > 1)
                {
                    //
                    // der pfadname an der klasse ist länger als die
                    // aktuellen
                    // namespacepfade, also werden diese verlängert
                    //
                    return new NamespaceNode(this, tail.first()).getClassNode(tail);
                }
                else
                {
                    //
                    // endlich ist der klassenname bekannt
                    //
                    return new ClassNode(this, new CapitalIdentifierNode(tail.first()).identifier());
                }
            }
        }
    }

    public Node append(final Node node) throws Exception
    {
        if(this
                .forEachChild(
                        (new NodeDispatcher<Boolean>()
                            {
                                private Node my2Check = null;

                                public NodeDispatcherResult<Boolean> visit(final NamespaceNode node) throws Exception
                                {
                                    if(node.identifier().equals(this.my2Check.identifier()))
                                    {
                                        throw new ParserException(
                                                ParserException.ExceptionType.NAMESPACE_DOUBLED,
                                                node.identifier());
                                    }
                                    else
                                    {
                                        return null;
                                    }
                                }

                                public NodeDispatcherResult<Boolean> visit(final ClassNode node) throws Exception
                                {
                                    if(node.identifier().equals(this.my2Check.identifier()))
                                    {
                                        throw new ParserException(
                                                ParserException.ExceptionType.CLASS_DOUBLED,
                                                node.identifier());
                                    }
                                    else
                                    {
                                        return null;
                                    }
                                }

                                public NodeDispatcher<Boolean> init(final Node toCheck)
                                {
                                    this.my2Check = toCheck;
                                    return this;
                                }
                            }).init(node),
                        (NodeDispatcherResult<Boolean> nodeDispatcherResult) ->
                            {
                                return (nodeDispatcherResult == null) ? false : nodeDispatcherResult.value();
                            },
                        (NodeDispatcherResult<Boolean> nodeDispatcherResult) ->
                            {
                                return nodeDispatcherResult;
                            },
                        (Node anyNode) ->
                            {
                                return new NodeDispatcherResult<Boolean>(true);
                            })
                .value())
        {
            return super.append(node);
        }
        else
        {
            return null;
        }
    }
}
