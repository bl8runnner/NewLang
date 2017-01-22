/**
 * 
 */
package expressions;

import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ExpressionNodeStepListByComma extends ExpressionNodeStepList
{
    private final static String IDENTIFIER = "COMMALIST";

    public ExpressionNodeStepListByComma() throws Exception
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
