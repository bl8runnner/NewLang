package expressions;

import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeBracketCurlyRight extends ExpressionNodeOneOp
{
    private final static String IDENTIFIER = "}";

    public static ExpressionNodeBracketCurlyRight create() throws Exception
    {
        return new ExpressionNodeBracketCurlyRight(ExpressionNodeEmpty.create());
    }

    public static ExpressionNodeBracketCurlyRight create(final Expressions expressions) throws Exception
    {
        return create(expressions.popExpression());
    }

    public static ExpressionNodeBracketCurlyRight create(final Node child) throws Exception
    {
        return new ExpressionNodeBracketCurlyRight(child);
    }

    private ExpressionNodeBracketCurlyRight(final Node child) throws Exception
    {
        super((child == null) ? ExpressionNodeEmpty.create() : child, IDENTIFIER);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
