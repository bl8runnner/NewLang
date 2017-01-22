package expressions;

import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeBracketRoundLeft extends ExpressionNodeOneOp
{
    private final static String IDENTIFIER = "(";

    public static ExpressionNodeBracketRoundLeft create() throws Exception
    {
        return create(ExpressionNodeEmpty.create());
    }

    public static ExpressionNodeBracketRoundLeft create(final Expressions expressions) throws Exception
    {
        return create(expressions.popExpression());
    }

    public static ExpressionNodeBracketRoundLeft create(final Node child) throws Exception
    {
        return new ExpressionNodeBracketRoundLeft(child);
    }

    private ExpressionNodeBracketRoundLeft(final Node child) throws Exception
    {
        super((child == null) ? ExpressionNodeEmpty.create() : child, IDENTIFIER);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
