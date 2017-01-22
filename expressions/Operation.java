package expressions;

import exception.FunctionWithException;
import exception.ParserException;
import expressions.Expressions.Type;
import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class Operation
{
    public enum OperationType
    {
        NULL(
                0,
                (Expressions expressions) ->
                    {
                        return Expressions.Type.NONE;
                    }),

        ROUND_BRACKET(
                1,
                (Expressions expressions) ->
                    {
                        return Expressions.Type.NONE;
                    }),

        CURLY_BRACKET(
                1,
                (Expressions expressions) ->
                    {
                        return Expressions.Type.NONE;
                    }),

        EDGE_BRACKET(
                1,
                (Expressions expressions) ->
                    {
                        return Expressions.Type.NONE;
                    }),

        POINTER(
                2,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodePointer.create(expressions));
                    }),

        CALL(
                3,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeCall.create(expressions));
                    }),

        FOR(
                3,
                (Expressions expressions) ->
                    {
                        expressions.popExpression().accept(new NodeDispatcher<Boolean>()
                            {
                                public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketRoundLeft node) throws Exception
                                {
                                    return null;
                                }

                            });

                        return Expressions.Type.EXPRESSION;
                    }),

        INC(
                4,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeInc.create(expressions));
                    }),

        DEC(
                4,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeDec.create(expressions));
                    }),

        NOT(
                4,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeNot.create(expressions));
                    }),

        CAST(
                4,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeTypeCast.create(expressions));
                    }),

        NEGATIVE_SIGN(
                5,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeNeg.create(expressions));
                    }),

        POSITIVE_SIGN(
                5,
                (Expressions expressions) ->
                    {
                        return Expressions.Type.NONE;
                    }),

        MUL(
                6,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeMul.create(expressions));
                    }),

        DIV(
                6,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeDiv.create(expressions));
                    }),

        MOD(
                6,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeMod.create(expressions));
                    }),

        ADD(
                7,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeAdd.create(expressions));
                    }),

        SUB(
                7,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeSub.create(expressions));
                    }),

        LESS(
                8,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeLess.create(expressions));
                    }),

        LESS_EQUAL(
                8,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeLessEqual.create(expressions));
                    }),

        GREATER(
                8,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeGreater.create(expressions));
                    }),

        GREATER_EQUAL(
                8,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeGreaterEqual.create(expressions));
                    }),

        EQUAL(
                8,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeEqual.create(expressions));
                    }),

        NOT_EQUAL(
                8,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeNot.create(ExpressionNodeEqual.create(expressions)));
                    }),

        XOR(
                9,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeXor.create(expressions));
                    }),

        AND(
                10,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeAnd.create(expressions));
                    }),

        OR(
                11,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeOr.create(expressions));
                    }),

        ASSIGN(
                12,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeAssign.create(expressions));
                    }),

        ADD_ASSIGN(
                12,
                (Expressions expressions) ->
                    {
                        final Node right = expressions.popExpression();
                        final Node left = expressions.popExpression();

                        return expressions.push(ExpressionNodeAssign.create(left, ExpressionNodeAdd.create(left, right)));
                    }),

        SUB_ASSIGN(
                12,
                (Expressions expressions) ->
                    {
                        final Node right = expressions.popExpression();
                        final Node left = expressions.popExpression();

                        return expressions.push(ExpressionNodeAssign.create(left, ExpressionNodeSub.create(left, right)));
                    }),

        MUL_ASSIGN(
                12,
                (Expressions expressions) ->
                    {
                        final Node right = expressions.popExpression();
                        final Node left = expressions.popExpression();

                        return expressions.push(ExpressionNodeAssign.create(left, ExpressionNodeMul.create(left, right)));
                    }),

        MOD_ASSIGN(
                12,
                (Expressions expressions) ->
                    {
                        final Node right = expressions.popExpression();
                        final Node left = expressions.popExpression();

                        return expressions.push(ExpressionNodeAssign.create(left, ExpressionNodeMod.create(left, right)));
                    }),

        DIV_ASSIGN(
                12,
                (Expressions expressions) ->
                    {
                        final Node right = expressions.popExpression();
                        final Node left = expressions.popExpression();

                        return expressions.push(ExpressionNodeAssign.create(left, ExpressionNodeDiv.create(left, right)));
                    }),

        AND_ASSIGN(
                12,
                (Expressions expressions) ->
                    {
                        final Node right = expressions.popExpression();
                        final Node left = expressions.popExpression();

                        return expressions.push(ExpressionNodeAssign.create(left, ExpressionNodeAnd.create(left, right)));
                    }),

        OR_ASSIGN(
                12,
                (Expressions expressions) ->
                    {
                        final Node right = expressions.popExpression();
                        final Node left = expressions.popExpression();

                        return expressions.push(ExpressionNodeAssign.create(left, ExpressionNodeOr.create(left, right)));
                    }),

        XOR_ASSIGN(
                12,
                (Expressions expressions) ->
                    {
                        final Node right = expressions.popExpression();
                        final Node left = expressions.popExpression();

                        return expressions.push(ExpressionNodeAssign.create(left, ExpressionNodeXor.create(left, right)));
                    }),

        VARIABLE(
                13,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeVariable.create(expressions));
                    }),

        COLON(
                13,
                (Expressions expressions) ->
                    {
                        return expressions.push(ExpressionNodeColon.create(expressions));
                    }),

        COMMA(
                14,
                (Expressions expressions) ->
                    {
                        //
                        // die kommawerte werden zu einer liste zusammengeklebt
                        //
                        final Node right = expressions.popExpression();

                        if(right == null)
                        {
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_RIGHT_OPERATOR);
                        }
                        else
                        {
                            final Node left = expressions.popExpression();

                            if(left == null)
                            {
                                throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_LEFT_OPERATOR);
                            }
                            else
                            {
                                //
                                // ein paar besonderheiten sind hier noch
                                // abzutesten
                                //
                                if(right.accept((new NodeDispatcher<Boolean>()
                                    {
                                        private Node myLeft        = null;

                                        private Expressions myExpressions = null;

                                        //
                                        // sollte jetzt das rechte Element ein
                                        // einfacher bezeichner sein,
                                        // so ist damit das ende der Liste
                                        // gekennzeichnet und es wird jetzt
                                        // eine stepListe aufgebaut, welche die
                                        // einzelnen elemente aufnimmt
                                        // rekursiv durchlaufen wir jetzt diesen
                                        // Dispatcher und ein linkes
                                        // element wird dann in die Liste
                                        // gehängt. Dann wird die liste auf
                                        // den stack gelegt
                                        //
                                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeIdentifier node) throws Exception
                                        {
                                            final ExpressionNodeStepListByComma stepList = new ExpressionNodeStepListByComma();

                                            stepList.append(node);
                                            return stepList.accept(this);
                                        }

                                        //
                                        // für praktisch alle gegebenen listen
                                        // wird ein dummyelement erzeugt,
                                        // damit zu einen diese stepliste
                                        // erstellt wird und zu anderen leere
                                        // klammerausdrücke mit einer leeren
                                        // stepliste erzeugt werden
                                        //
                                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeEmpty node) throws Exception
                                        {
                                            return new ExpressionNodeStepListByComma().accept(this);
                                        }

                                        //
                                        // eine stepliste wurde entweder neu
                                        // erstellt und jetzt gibt es einen
                                        // rekursiven durchlauf oder aber die
                                        // stepliste liegt auf dem stack
                                        // und das linke element wird eingehängt
                                        //
                                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeStepListByComma node) throws Exception
                                        {
                                            //
                                            // doppelt leere einträge führen nur
                                            // dazu, dass nichts in die liste
                                            // eingetragen wird
                                            //
                                            if(this.myLeft.accept(new NodeDispatcher<Boolean>()
                                                {
                                                    public NodeDispatcherResult<Boolean> visit(final ExpressionNodeEmpty node) throws Exception
                                                    {
                                                        return new NodeDispatcherResult<Boolean>(true);
                                                    }
                                                }) == null)
                                                node.append(this.myLeft);
                                            this.myExpressions.push(node);
                                            return new NodeDispatcherResult<Boolean>(true);
                                        }

                                        public NodeDispatcher<Boolean> init(final Expressions expressions, final Node left)
                                        {
                                            this.myLeft = left;
                                            this.myExpressions = expressions;
                                            return this;
                                        }

                                    }).init(expressions, left)) == null)
                                {
                                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_WRONG_STEPLIST);
                                }
                            }
                        }

                        return Expressions.Type.NONE;
                    }),

        SEMICOLON(
                14,
                (Expressions expressions) ->
                    {
                        //
                        // die kommawerte werden zu einer liste zusammengeklebt
                        //
                        final Node right = expressions.popExpression();

                        if(right == null)
                        {
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_RIGHT_OPERATOR);
                        }
                        else
                        {
                            final Node left = expressions.popExpression();

                            if(left == null)
                            {
                                //
                                // es kann durchaus sein, dass die listen schon
                                // komplett
                                // aufgebaut sind und jetzt noch ein paar
                                // semikolons vorhanden
                                // sein. daher gibt es keine linken einträge
                                // mehr, aber auf der
                                // rechten seite sind die steplisten vorhanden.
                                // diese werden einfach
                                // wieder zurückgegeben
                                //
                                if(right.accept((new NodeDispatcher<Boolean>()
                                    {
                                        private Expressions myExpressions = null;

                                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeStepListBySemicolon node) throws Exception
                                        {
                                            this.myExpressions.push(node);
                                            return new NodeDispatcherResult<Boolean>(true);
                                        }

                                        public NodeDispatcher<Boolean> init(final Expressions expressions)
                                        {
                                            this.myExpressions = expressions;
                                            return this;
                                        }
                                    }).init(expressions)) == null)
                                {
                                    final ExpressionNodeStepListBySemicolon stepList = new ExpressionNodeStepListBySemicolon();

                                    stepList.append(right);
                                    expressions.push(stepList);
                                }
                            }
                            else
                            {
                                //
                                // ein paar besonderheiten sind hier noch
                                // abzutesten
                                //
                                if(right.accept((new NodeDispatcher<Boolean>()
                                    {
                                        private Node myLeft        = null;

                                        private Expressions myExpressions = null;

                                        //
                                        // sollte jetzt das rechte Element ein
                                        // einfacher bezeichner sein,
                                        // so ist damit das ende der Liste
                                        // gekennzeichnet und es wird jetzt
                                        // eine stepListe aufgebaut, welche die
                                        // einzelnen elemente aufnimmt
                                        // rekursiv durchlaufen wir jetzt diesen
                                        // Dispatcher und ein linkes
                                        // element wird dann in die Liste
                                        // gehängt. Dann wird die liste auf
                                        // den stack gelegt
                                        //
                                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeIdentifier node) throws Exception
                                        {
                                            final ExpressionNodeStepListBySemicolon stepList = new ExpressionNodeStepListBySemicolon();

                                            stepList.append(node);
                                            return stepList.accept(this);
                                        }

                                        //
                                        // für praktisch alle gegebenen listen
                                        // wird ein dummyelement erzeugt,
                                        // damit zu einen diese stepliste
                                        // erstellt wird und zu anderen leere
                                        // klammerausdrücke mit einer leeren
                                        // stepliste erzeugt werden
                                        //
                                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeEmpty node) throws Exception
                                        {
                                            return new ExpressionNodeStepListBySemicolon().accept(this);
                                        }

                                        //
                                        // eine stepliste wurde entweder neu
                                        // erstellt und jetzt gibt es einen
                                        // rekursiven durchlauf oder aber die
                                        // stepliste liegt auf dem stack
                                        // und das linke element wird eingehängt
                                        //
                                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeStepListBySemicolon node) throws Exception
                                        {
                                            //
                                            // doppelt leere einträge führen nur
                                            // dazu, dass nichts in die liste
                                            // eingetragen wird
                                            //
                                            if(this.myLeft.accept(new NodeDispatcher<Boolean>()
                                                {
                                                    public NodeDispatcherResult<Boolean> visit(final ExpressionNodeEmpty node) throws Exception
                                                    {
                                                        return new NodeDispatcherResult<Boolean>(true);
                                                    }
                                                }) == null)
                                                node.append(this.myLeft);
                                            this.myExpressions.push(node);
                                            return new NodeDispatcherResult<Boolean>(true);
                                        }

                                        public NodeDispatcher<Boolean> init(final Expressions expressions, final Node left)
                                        {
                                            this.myLeft = left;
                                            this.myExpressions = expressions;
                                            return this;
                                        }

                                    }).init(expressions, left)) == null)
                                {
                                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_WRONG_STEPLIST);
                                }
                            }
                        }

                        return Expressions.Type.NONE;
                    }),

        IDENTIFIER(
                15,
                (Expressions expressions) ->
                    {
                        return Expressions.Type.NONE;
                    }),

        ROUND_BRACKET_LEFT(
                16,
                (Expressions expressions) ->
                    {
                        //
                        // prüfe, ob es eine rechte Klammer gibt
                        //
                        if(expressions.topExpression().accept(new NodeDispatcher<Boolean>()
                            {
                                public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketRoundRight node) throws Exception
                                {
                                    return new NodeDispatcherResult<Boolean>(true);
                                }

                            }) != null)
                        {
                            return Type.EXPRESSION;
                        }
                        else
                        {
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_MATCHING_BRACKET);
                        }
                    }),

        CURLY_BRACKET_LEFT(
                16,
                (Expressions expressions) ->
                    {
                        //
                        // prüfe, ob es eine rechte Klammer gibt
                        //
                        if(expressions.topExpression().accept(new NodeDispatcher<Boolean>()
                            {
                                public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketCurlyRight node) throws Exception
                                {
                                    return new NodeDispatcherResult<Boolean>(true);
                                }

                            }) != null)
                        {
                            return Type.EXPRESSION;
                        }
                        else
                        {
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_MATCHING_BRACKET);
                        }
                    }),

        EDGE_BRACKET_LEFT(
                16,
                (Expressions expressions) ->
                    {
                        //
                        // prüfe, ob es eine rechte Klammer gibt
                        //
                        if(expressions.topExpression().accept(new NodeDispatcher<Boolean>()
                            {
                                public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketEdgeRight node) throws Exception
                                {
                                    return new NodeDispatcherResult<Boolean>(true);
                                }

                            }) != null)
                        {
                            return Type.EXPRESSION;
                        }
                        else
                        {
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_MATCHING_BRACKET);
                        }
                    });

        private FunctionWithException<Expressions, Expressions.Type> myStackFunction = null;

        private int                                                  myPriority      = 0;

        public int priority()
        {
            return this.myPriority;
        }

        OperationType(final int priority)
        {
            this(priority, null);
        }

        OperationType(final int priority, final FunctionWithException<Expressions, Expressions.Type> stackFunction)
        {
            this.myPriority = priority;
            this.myStackFunction = stackFunction;
        }

        int compare(final OperationType other)
        {
            return (other == null) ? 1 : ((this.priority() == other.priority()) ? 0 : ((this.priority() < other.priority()) ? -1 : 1));
        }

        Expressions.Type function(final Expressions expressions) throws Exception
        {
            return this.myStackFunction.apply(expressions);
        }
    }
}
