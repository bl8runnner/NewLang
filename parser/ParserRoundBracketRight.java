package parser;

import exception.ParserException;
import expressions.ExpressionNodeBracketRoundRight;
import expressions.ExpressionNodeIdentifier;
import expressions.ExpressionNodePointer;
import expressions.Expressions;
import expressions.Operation;
import expressions.Expressions.Type;
import misc.SourceStream;
import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ParserRoundBracketRight implements Parser<Type, ParserArg4ExpressionWithType>, KeywordChecker
{

    public ParserRoundBracketRight()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isKeyword(final String identifier) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Type parse(final SourceStream stream, final Node parent, final ParserArg4ExpressionWithType args) throws Exception
    {
        //
        // der ausdruck wird jetzt mit einer klammer abgeschlossen.
        // wenn der klammerausdruck korrekt ist, dann sollte jetzt
        // eine schließende klammer zurückgeliefert werden. diese
        // annahme wird über den visitor überprüft
        //
        if(args.getExpressions().checkBrackets(args.getType(), ExpressionNodeBracketRoundRight.create()).accept((new NodeDispatcher<Boolean>()
            {
                private Expressions myExpressions = null;

                public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketRoundRight node) throws Exception
                {
                    node.add(this.myExpressions.popExpression());
                    this.myExpressions.push(node);
                    return new NodeDispatcherResult<Boolean>(true);
                }

                public NodeDispatcher<Boolean> init(final Expressions expressions)
                {
                    this.myExpressions = expressions;
                    return this;
                }

            }).init(args.getExpressions())) == null)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_MATCHING_BRACKET);
        }
        else
        {
            return Expressions.Type.TERMINATE;
        }
    }
}
