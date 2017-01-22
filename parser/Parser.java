package parser;

import misc.SourceStream;
import node.Node;

public interface Parser<R, A>
{
    public R parse(final SourceStream stream, final Node parent, final A args) throws Exception;

}
