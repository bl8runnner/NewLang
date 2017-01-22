package expressions;

import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeBracketRoundRight extends ExpressionNodeOneOp
{
    private final static String IDENTIFIER = ")";

    public static ExpressionNodeBracketRoundRight create() throws Exception
    {
        return new ExpressionNodeBracketRoundRight(ExpressionNodeEmpty.create());
    }

    public static ExpressionNodeBracketRoundRight create(final Expressions expressions) throws Exception
    {
        return create(expressions.popExpression());
    }

    public static ExpressionNodeBracketRoundRight create(final Node child) throws Exception
    {
        return new ExpressionNodeBracketRoundRight(child);
    }

    private ExpressionNodeBracketRoundRight(final Node child) throws Exception
    {
        super((child == null) ? ExpressionNodeEmpty.create() : child, IDENTIFIER);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        return visitor.visit(this);
    }

}
