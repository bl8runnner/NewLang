package expressions;

import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeBracketCurlyLeft extends ExpressionNodeOneOp
{
    private final static String IDENTIFIER = "{";

    public static ExpressionNodeBracketCurlyLeft create() throws Exception
    {
        return create(ExpressionNodeEmpty.create());
    }

    public static ExpressionNodeBracketCurlyLeft create(final Expressions expressions) throws Exception
    {
        return create(expressions.popExpression());
    }

    public static ExpressionNodeBracketCurlyLeft create(final Node child) throws Exception
    {
        return new ExpressionNodeBracketCurlyLeft(child);
    }

    private ExpressionNodeBracketCurlyLeft(final Node child) throws Exception
    {
        super((child == null) ? ExpressionNodeEmpty.create() : child, IDENTIFIER);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
