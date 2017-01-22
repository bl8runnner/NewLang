/**
 * 
 */
package expressions;

import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeStepListBySemicolon extends ExpressionNodeStepList
{
    private final static String IDENTIFIER = "SEMICOLONLIST";

    public ExpressionNodeStepListBySemicolon() throws Exception
    {
        super(IDENTIFIER);
    }

    @Override
    public <T> NodeDispatcherResult<T> accept(final NodeDispatcher<T> visitor) throws Exception
    {
        // TODO Auto-generated method stub
        return visitor.visit(this);
    }

}
