package parser;

import expressions.ExpressionNodeIdentifier;
import expressions.ExpressionNodePointer;
import expressions.Expressions;
import expressions.Operation;
import expressions.Expressions.Type;
import misc.SourceStream;
import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class ParserRoundBracketLeft implements Parser<Type, ParserRoundBracketLeftArg>, KeywordChecker
{

    public ParserRoundBracketLeft()
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
    public Type parse(final SourceStream stream, final Node parent, final ParserRoundBracketLeftArg args) throws Exception
    {
        //
        // der klammerausdruck wird über einen rekursiven Aufruf
        // des parsers ausgewertet. Für den rekursiven ausdruck
        // wird eine linke Klammer zuerst auf den operationstack
        // gelegt. Der ausdruck in der Klammer wird bei korrektem
        // aufbau soweit abgebaut, dass nur noch diese linke klammer
        // auf dem stack liegt und damit das ende des ausdrucks
        // angezeigt wird anschließend wird geprüft, ob sich links
        // ein bezeichner befindet (dann ist es ein call) oder ein
        // anderer klammerausdruck (dann ist es ein cast)
        //
        switch(args.getType())
        {
            case NONE:
            case OPERATION:
                //
                // ein klammerausdruck steht am anfang eines
                // ausdrucks oder direkt hinter einer operation.
                // der klammerausdruck wird nur auf den stack
                // für ausdrücke hinterlegt
                //
                args.getExpressions().push(new Expressions(stream, Operation.OperationType.ROUND_BRACKET_LEFT).process(args.getValidDelimeter()));
                return Type.BRACKET;

            case BRACKET:
                //
                // das wird ein cast
                //
                args.getExpressions().push(new Expressions(stream, Operation.OperationType.ROUND_BRACKET_LEFT).process(args.getValidDelimeter()));
                args.getExpressions().push(Operation.OperationType.CAST);
                return Type.BRACKET;

            case EXPRESSION:
                if(args.getExpressions().operations() > 0)
                {
                    //
                    // auf der linken Seite könnte jetzt ein
                    // Pointer liegen und der hat vorrang
                    //
                    if(args.getExpressions().topOperation() == Operation.OperationType.POINTER)
                    {
                        args.getExpressions().popOperation().function(args.getExpressions());
                    }
                }

                if(args.getExpressions().topExpression().accept(
                        (new NodeDispatcher<Boolean>()
                            {
                                private Expressions myExpressions = null;

                                private Node myBracketNode = null;

                                public NodeDispatcher<Boolean> init(final Expressions expressions, final Node bracketNode) throws Exception
                                {
                                    this.myExpressions = expressions;
                                    this.myBracketNode = bracketNode;
                                    return this;
                                }

                                //
                                // gibt es links von dem klammeroperator einen
                                // identifier so wird es ein methodenaufruf sein
                                //
                                public NodeDispatcherResult<Boolean> visit(final ExpressionNodeIdentifier node) throws Exception
                                {
                                    this.myExpressions.push(this.myBracketNode);
                                    this.myExpressions.push(Operation.OperationType.CALL);
                                    return new NodeDispatcherResult<Boolean>(true);
                                }

                                //
                                // gibt es links von dem klammeroperator einen
                                // pointer so wird es ein methodenaufruf sein
                                //
                                public NodeDispatcherResult<Boolean> visit(final ExpressionNodePointer node) throws Exception
                                {
                                    this.myExpressions.push(this.myBracketNode);
                                    this.myExpressions.push(Operation.OperationType.CALL);
                                    return new NodeDispatcherResult<Boolean>(true);
                                }
                            }).init(args.getExpressions(), new Expressions(stream, Operation.OperationType.ROUND_BRACKET_LEFT).process(args.getValidDelimeter()))) != null)
                {
                    return Expressions.Type.EXPRESSION;
                }

            default:
                return Expressions.Type.INVALID;
        }
    }
}
