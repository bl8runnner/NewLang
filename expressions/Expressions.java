package expressions;

import exception.ParserException;
import misc.SimpleStack;
import misc.SourceStream;
import node.Node;
import node.NodeDispatcher;
import node.NodeDispatcherResult;

public class Expressions
{
    //
    // über diesen Typen wird hinterlegt, was zuletzt abgelegt wurde.
    // entsprechend dieses letzen Typs ist der aktuelle Ausdruck korrekt
    // oder fehlerhaft
    //
    public enum Type
    {
        INVALID, // das ist ein zweifelhaftes Ergebnis
        TERMINATE, // ein klammerausdruck ist erfolgreich abgearbeitet
        BRACKET, // ein klammerausdruck liegt vor
        NONE, // es wurden weder eine Operation noch ein Ausdruck eingegeben
        OPERATION, // es wurde zuletzt eine Operation verarbeitet
        EXPRESSION; // es wurde zuletzt ein beliebiger Ausdruck verarbeitet
    }

    private char myValidDelimeter = 0;

    public final char validDelimeter()
    {
        return this.myValidDelimeter;
    }

    private final void validDelimeter(final char validDelimeter)
    {
        this.myValidDelimeter = validDelimeter;
    }

    private SourceStream myStream = null;

    public final SourceStream stream()
    {
        return this.myStream;
    }

    private String nextBlancStreamResult() throws Exception
    {
        this.stream().consumeBlancs();
        return this.stream().check4Next();
    }

    private final SimpleStack<Operation.OperationType> myOperationStack = new SimpleStack<Operation.OperationType>();

    public final SimpleStack<Operation.OperationType> operationStack()
    {
        return this.myOperationStack;
    }

    private final SimpleStack<Node> myExpressionStack = new SimpleStack<Node>();

    public final SimpleStack<Node> expressionStack()
    {
        return this.myExpressionStack;
    }

    private final static String BRACKET_LEFT        = "(";

    private final static String BRACKET_RIGHT       = ")";

    private final static String CURLY_BRACKET_LEFT  = "{";

    private final static String CURLY_BRACKET_RIGHT = "}";

    private final static String EDGE_BRACKET_LEFT   = "[";

    private final static String EDGE_BRACKET_RIGHT  = "]";

    private final static String DOT                 = ".";

    private final static String POINTER             = "^";

    private final static String PLUS                = "+";

    private final static String MINUS               = "-";

    private final static String MUL                 = "*";

    private final static String DIV                 = "/";

    private final static String MOD                 = "%";

    private final static String LESS                = "<";

    private final static String GREATER             = ">";

    private final static String SHOUT               = "!";

    private final static String EQUAL               = "=";

    private final static String COLON               = ":";

    private final static String AND                 = "&";

    private final static String OR                  = "|";

    private final static String XOR                 = "^";

    private final static String COMMA               = ",";

    private final static String SEMICOLON           = ";";

    private final static String LOWER_EXP           = "e";

    private final static String UPPER_EXP           = "E";

    private final static String TRUE                = "true";

    private final static String FALSE               = "false";

    public final Node popExpression()
    {
        return this.myExpressionStack.pop();
    }

    public final Operation.OperationType popOperation()
    {
        return this.myOperationStack.pop();
    }

    public final Node topExpression()
    {
        return this.myExpressionStack.top();
    }

    public final Operation.OperationType topOperation()
    {
        return this.myOperationStack.top();
    }

    public final int expressions()
    {
        return this.myExpressionStack.size();
    }

    public final int operations()
    {
        return this.myOperationStack.size();
    }

    public final Type push(final Node node)
    {
        this.myExpressionStack.push(node);
        return Type.EXPRESSION;
    }

    public final Type push(final Operation.OperationType operation) throws Exception
    {
        if(this.operations() > 0)
        {
            switch(operation.compare(this.topOperation()))
            {
                case 1:
                    this.popOperation().function(this);
                    return this.push(operation);

                case 0:
                case -1:
                    break;
            }
        }

        this.myOperationStack.push(operation);
        return Type.OPERATION;
    }

    //
    // verarbeite '('
    //
    private Type checkRoundBracketLeft(final char validDelimeter, final Type type) throws Exception
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
        switch(type)
        {
            case NONE:
            case OPERATION:
                //
                // ein klammerausdruck steht am anfang eines
                // ausdrucks oder direkt hinter einer operation.
                // der klammerausdruck wird nur auf den stack
                // für ausdrücke hinterlegt
                //
                this.push(new Expressions(this.stream(), Operation.OperationType.ROUND_BRACKET_LEFT).process(validDelimeter));
                return Type.BRACKET;

            case BRACKET:
                //
                // das wird ein cast
                //
                this.push(new Expressions(this.stream(), Operation.OperationType.ROUND_BRACKET_LEFT).process(validDelimeter));
                this.push(Operation.OperationType.CAST);
                return Type.BRACKET;

            case EXPRESSION:
                if(this.operations() > 0)
                {
                    //
                    // auf der linken Seite könnte jetzt ein
                    // Pointer liegen und der hat vorrang
                    //
                    if(this.topOperation() == Operation.OperationType.POINTER)
                    {
                        this.popOperation().function(this);
                    }
                }

                if(this.topExpression().accept(
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
                            }).init(this, new Expressions(this.stream(), Operation.OperationType.ROUND_BRACKET_LEFT).process(validDelimeter))) != null)
                {
                    return Expressions.Type.EXPRESSION;
                }

            default:
                return Expressions.Type.INVALID;
        }
    }

    //
    // verarbeite '{'
    //
    private Type checkCurlyBracketLeft(final Type type) throws Exception
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
        switch(type)
        {
            case NONE:
            case BRACKET:
            case OPERATION:
                //
                // ein klammerausdruck steht am anfang eines
                // ausdrucks oder direkt hinter einer operation.
                // der klammerausdruck wird nur auf den stack
                // für ausdrücke hinterlegt
                //
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_INVALID_BLOCK_DEFINITION);

            case EXPRESSION:
                this.push(new Expressions(this.stream(), Operation.OperationType.CURLY_BRACKET_LEFT).process());
                return Expressions.Type.EXPRESSION;

            default:
                return Expressions.Type.INVALID;
        }
    }

    //
    // verarbeite '['
    //
    private Type checkEdgeBracketLeft(final Type type) throws Exception
    {
        //
        // der klammerausdruck wird über einen rekursiven Aufruf
        // des parsers ausgewertet und auf den Stack gelegt
        //
        return this.push(new Expressions(this.stream(), Operation.OperationType.EDGE_BRACKET_LEFT).process());
    }

    //
    // verarbeite ')', '}', ']'
    //
    private Node checkBrackets(final Type type, final Node node) throws Exception
    {
        if(this.operations() == 0)
        {
            //
            // dann würde es keine geöffnete klammer geben
            //
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_MATCHING_BRACKET);
        }
        else
        {
            //
            // verarbeite den Operation bis zum letzten Element
            // (welches eine klammer sein sollte - wenn nicht, gibt
            // es eine fehlermeldung)
            // und schiebe dann ein passendes klammerelement dazu
            // in der abschlussfunktion wird dann auf die klammerkorrektheit
            // geprüft
            //
            if(this.expressions() == 0)
            {
                //
                // das ist jetzt eine leere klammer
                //
                node.accept((new NodeDispatcher<Boolean>()
                    {
                        private Expressions myExpressions = null;

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketCurlyRight node) throws Exception
                        {
                            this.myExpressions.push(new ExpressionNodeStepListBySemicolon());
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketEdgeRight node) throws Exception
                        {
                            //
                            // das kommt bei einem 'compare vor
                            //
                            this.myExpressions.push(ExpressionNodeEmpty.create());
                            this.myExpressions.push(ExpressionNodeEmpty.create());
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketRoundRight node) throws Exception
                        {
                            //
                            // das wäre jetzt eine leere klammer und damit
                            // für einen methodenausdruck korrekt
                            //
                            this.myExpressions.push(new ExpressionNodeStepListByComma());
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcher<Boolean> init(final Expressions expressions)
                        {
                            this.myExpressions = expressions;
                            return this;
                        }
                    }).init(this));
            }
            else
            {
                //
                // es wird ein dummy-element angehängt. damit wird
                // bei der verarbeitung der einträge eine steplist
                // erzeugt
                //
                node.accept((new NodeDispatcher<Boolean>()
                    {

                        private Expressions myExpressions = null;

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketCurlyRight node) throws Exception
                        {
                            this.myExpressions.push(Operation.OperationType.SEMICOLON);
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketEdgeRight node) throws Exception
                        {
                            this.myExpressions.push(Operation.OperationType.SEMICOLON);
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketRoundRight node) throws Exception
                        {
                            switch(this.myExpressions.validDelimeter())
                            {
                                case ';':
                                    this.myExpressions.push(Operation.OperationType.SEMICOLON);
                                    break;

                                default:
                                    this.myExpressions.push(Operation.OperationType.COMMA);
                            }

                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcher<Boolean> init(final Expressions expressions)
                        {
                            this.myExpressions = expressions;
                            return this;
                        }

                    }).init(this));

                this.push(ExpressionNodeEmpty.create());
            }

            while(this.operations() > 1)
            {
                //
                // mit ausnahme der ersten operation, die eine geöffnete
                // klammer sein sollte, wird jetzt alles abgeräumt
                //
                this.popOperation().function(this);
            }

            this.push(node);
            this.popOperation().function(this);
            return this.popExpression();
        }
    }

    //
    // verarbeite ')'
    //
    private Type checkBracketRoundRight(final Type type) throws Exception
    {
        //
        // der ausdruck wird jetzt mit einer klammer abgeschlossen.
        // wenn der klammerausdruck korrekt ist, dann sollte jetzt
        // eine schließende klammer zurückgeliefert werden. diese
        // annahme wird über den visitor überprüft
        //
        if(this.checkBrackets(type, ExpressionNodeBracketRoundRight.create()).accept((new NodeDispatcher<Boolean>()
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

            }).init(this)) == null)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_MATCHING_BRACKET);
        }
        else
        {
            return Expressions.Type.TERMINATE;
        }
    }

    //
    // verarbeite ']'
    //
    private Type checkBracketEdgeRight(final Type type) throws Exception
    {
        if(this.checkBrackets(type, ExpressionNodeBracketEdgeRight.create()).accept((new NodeDispatcher<Boolean>()
            {
                private Expressions myExpressions = null;

                public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketEdgeRight node) throws Exception
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

            }).init(this)) == null)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_MATCHING_BRACKET);
        }
        else
        {
            return Expressions.Type.TERMINATE;
        }
    }

    //
    // verarbeite '}'
    //
    private Type checkBracketCurlyRight(final Type type) throws Exception
    {
        //
        // der ausdruck wird jetzt mit einer klammer abgeschlossen.
        // wenn der klammerausdruck korrekt ist, dann sollte jetzt
        // eine schließende klammer zurückgeliefert werden. diese
        // annahme wird über den visitor überprüft
        //
        if(this.checkBrackets(type, ExpressionNodeBracketCurlyRight.create()).accept((new NodeDispatcher<Boolean>()
            {
                private Expressions myExpressions = null;

                public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketCurlyRight node) throws Exception
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

            }).init(this)) == null)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_MATCHING_BRACKET);
        }
        else
        {
            return Expressions.Type.TERMINATE;
        }
    }

    //
    // verarbeite '^'
    //
    private Type checkPointer(final Type type) throws Exception
    {
        switch(type)
        {
            case NONE:
            case OPERATION:
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_NO_OPERATION);

            case EXPRESSION:
            case BRACKET:
                //
                // es könnte aber auch das log. XOR werden
                //
                if(this.stream().check4Next().equals(POINTER))
                {
                    //
                    // OK, jetzt ist es auch ein XOR, da keine Blancs und
                    // ein POINTER
                    //
                    return this.push(Operation.OperationType.XOR);
                }
                else
                {
                    //
                    // lege das zweite zeichen zurück und werte den pointer
                    // aus
                    //
                    this.stream().toSnapshot();
                    return this.push(Operation.OperationType.POINTER);
                }

            default:
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_UNKNOWN_LAST_TYPE);
        }
    }

    //
    // prüfe, ob ein vorzeichen möglich ist
    //
    private Type trySign(final boolean isPositive, final Type type) throws Exception
    {
        switch(type)
        {
            case NONE:
                //
                // das ist das allererste zeichen und somit ein vorzeichen
                //
                return this.push(isPositive ? Operation.OperationType.POSITIVE_SIGN : Operation.OperationType.NEGATIVE_SIGN);

            case EXPRESSION:
            case BRACKET:
                //
                // das sollte jetzt eine Addition/Subtraktion werden
                // die linke Seite wird jetzt abgeprüft
                //
                if(this.topExpression().accept(new NodeDispatcher<Boolean>()
                    {
                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketRoundRight node) throws Exception
                        {
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeIdentifier node) throws Exception
                        {
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeIf node) throws Exception
                        {
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeFor node) throws Exception
                        {
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeCompare node) throws Exception
                        {
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeSwitch node) throws Exception
                        {
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeCall node) throws Exception
                        {
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeTypeCast node) throws Exception
                        {
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                    }) == null)
                {
                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_PLUS_MINUS_INVALID);
                }
                else
                {
                    //
                    // es wurde zuletzt ein bezeichner o.ä. auf den stack
                    // gelegt. damit ist es zwingend eine operation
                    //
                    return this.push(isPositive ? Operation.OperationType.ADD : Operation.OperationType.SUB);
                }

            case OPERATION:
                //
                // werte die Operation auf dem Stack aus
                //
                switch(this.topOperation())
                {
                    case ADD:
                    case ADD_ASSIGN:
                    case DIV:
                    case DIV_ASSIGN:
                    case MUL:
                    case MUL_ASSIGN:
                    case MOD:
                    case MOD_ASSIGN:
                    case SUB:
                    case SUB_ASSIGN:
                        //
                        // bei den rechenoperationen wird von einem
                        // Vorzeichen ausgegangen
                        //
                        return this.push(isPositive ? Operation.OperationType.POSITIVE_SIGN : Operation.OperationType.NEGATIVE_SIGN);

                    default:
                        //
                        // bei allem anderen wird eine rechenoperation
                        // angenommen
                        //
                        return this.push(isPositive ? Operation.OperationType.ADD : Operation.OperationType.SUB);
                }

            default:
                return Expressions.Type.INVALID;
        }
    }

    //
    // verarbeite '+', '-'
    //
    private Type checkPlusMinus(final String expression, final Type type) throws Exception
    {
        switch(type)
        {
            case OPERATION:
            case NONE:
            case EXPRESSION:
            case BRACKET:
                //
                // jetzt wird noch ermittelt, ob es ein INC/DEC oder eine
                // Zuweisung sein könnte
                //
                switch(this.stream().check4Next())
                {
                    case PLUS:
                        if(expression.equals(PLUS))
                        {
                            //
                            // ein '++'
                            //
                            return this.push(Operation.OperationType.INC);
                        }
                        else
                        {
                            //
                            // alles andere
                            //
                            this.stream().toSnapshot();
                            return this.trySign(expression.equals(PLUS), type);
                        }

                    case MINUS:
                        if(expression.equals(MINUS))
                        {
                            //
                            // ein '--'
                            //
                            return this.push(Operation.OperationType.DEC);
                        }
                        else
                        {
                            //
                            // alles andere
                            //
                            this.stream().toSnapshot();
                            return this.trySign(expression.equals(PLUS), type);
                        }

                    case COLON:
                        //
                        // könnte es eine Zuweisung werden ?
                        //
                        if(this.stream().check4Next().equals(EQUAL))
                        {
                            //
                            // yepp ! ein '+:=' oder '-:='
                            //
                            return this.push(expression.equals(PLUS) ? Operation.OperationType.ADD_ASSIGN : Operation.OperationType.SUB_ASSIGN);
                        }
                        else
                        {
                            //
                            // '+:' oder '-:' sind dummes zeug
                            //
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_EQUAL_IN_COMBINED_ASSIGNMENT);
                        }

                    default:
                        //
                        // nö, alles andere, könnte aber ein vorzeichen
                        // sein
                        //
                        this.stream().toSnapshot();
                        return this.trySign(expression.equals(PLUS), type);
                }

            default:
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_UNKNOWN_LAST_TYPE);
        }
    }

    //
    // verarbeite '*', '/', '%'
    //
    private Type checkMulDiv(final String expression, final Type type) throws Exception
    {
        switch(type)
        {
            case OPERATION:
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_NO_OPERATION);

            case NONE:
                //
                // sonderfall für kommentare
                //
                if(!expression.equals(DIV))
                {
                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_NO_OPERATION);
                }

            case EXPRESSION:
            case BRACKET:
                //
                // hier wird noch geprüft, ob es sich um eine kombinierte
                // Zuweisung handeln könnte
                //
                switch(this.stream().check4Next())
                {
                    case COLON:
                        //
                        // möglich wäre es
                        //
                        if(!this.stream().check4Next().equals(EQUAL))
                        {
                            //
                            // '*:' oder '/:' oder '%:' sind dummes zeug
                            //
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_EQUAL_IN_COMBINED_ASSIGNMENT);
                        }
                        else
                        {
                            //
                            // jetzt passt es
                            //
                            switch(expression)
                            {
                                case MUL:
                                    //
                                    // ein '*:='
                                    //
                                    return this.push(Operation.OperationType.MUL_ASSIGN);

                                case DIV:
                                    //
                                    // ein '/:='
                                    //
                                    return this.push(Operation.OperationType.DIV_ASSIGN);

                                case MOD:
                                    //
                                    // ein '%:='
                                    //
                                    return this.push(Operation.OperationType.MOD_ASSIGN);
                            }
                        }

                    case DIV:
                        if(expression.equals(DIV))
                        {
                            //
                            // kommentare -> bis zum rest der zeile wird
                            // überlesen
                            //
                            this.stream().nextLine();
                            return Expressions.Type.NONE;
                        }
                        else
                        {
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_OPERATOR_MIXMAX);
                        }

                    case MUL:
                        if(expression.equals(MUL))
                        {
                            //
                            // kommentare -> bis zum rest der zeile wird
                            // überlesen
                            //
                            this.stream().nextLine();
                            return Expressions.Type.NONE;
                        }
                        else
                        {
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_OPERATOR_MIXMAX);
                        }

                    default:
                        //
                        // zweites zeichen zurücklegen und den ursprung
                        // auswerten
                        //
                        this.stream().toSnapshot();

                        switch(expression)
                        {
                            case MUL:
                                return this.push(Operation.OperationType.MUL);

                            case MOD:
                                return this.push(Operation.OperationType.MOD);

                            case DIV:
                                return this.push(Operation.OperationType.DIV);

                        }
                }

                return Expressions.Type.INVALID;

            default:
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_UNKNOWN_LAST_TYPE);
        }
    }

    //
    // verarbeite '<', '<=', '>', '>=', '!', '==', '!='
    //
    private Type checkComparision(final String expression, final Type type) throws Exception
    {
        switch(type)
        {
            case NONE:
            case OPERATION:
            case EXPRESSION:
            case BRACKET:
                if(this.stream().check4Next().equals(EQUAL))
                {
                    //
                    // evtl. wird das ein Vergleich auf Gleichheit
                    //
                    switch(expression)
                    {
                        case LESS:
                            //
                            // ein '<='
                            //
                            return this.push(Operation.OperationType.LESS_EQUAL);

                        case GREATER:
                            //
                            // ein '>='
                            //
                            return this.push(Operation.OperationType.GREATER_EQUAL);

                        case EQUAL:
                            //
                            // ein '=='
                            //
                            return this.push(Operation.OperationType.EQUAL);

                        case SHOUT:
                            //
                            // ein '!='
                            //
                            return this.push(Operation.OperationType.NOT_EQUAL);

                    }
                }
                else
                {
                    //
                    // lege das zweite zeichen zurück
                    // bei '<' und '>' kommt die sonderbehandlung für
                    // 'compare'
                    // hinzu,
                    // da in diesem Fall diese Zeichen die allerersten sind
                    //
                    this.stream().toSnapshot();

                    switch(expression)
                    {
                        case LESS:
                            //
                            // ein '<'
                            //
                            return this.push(Operation.OperationType.LESS);

                        case GREATER:
                            //
                            // ein '>'
                            //
                            return this.push(Operation.OperationType.GREATER);

                        case SHOUT:
                            //
                            // ein '!'
                            //
                            return this.push(Operation.OperationType.NOT);

                        case EQUAL:
                            //
                            // das geht nicht !
                            //
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_SINGLE_EQUAL);

                    }
                }

                return Expressions.Type.INVALID;

            default:
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_UNKNOWN_LAST_TYPE);
        }
    }

    //
    // verarbeite ':=', ':=+', ':=-', ':=*', ':=/', ':=%', ':=&', ':=|', ':=^'
    //
    private Type checkAssignment(final Type type) throws Exception
    {
        switch(type)
        {
            case NONE:
            case OPERATION:
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_NO_OPERATION);

            case EXPRESSION:
            case BRACKET:
                if(this.stream().check4Next().equals(EQUAL))
                {
                    //
                    // könnte es eine zusammengesetzte Zuweisung sein ?
                    //
                    switch(this.stream().check4Next())
                    {
                        case PLUS:
                            //
                            // ':=+'
                            //
                            return this.push(Operation.OperationType.ADD_ASSIGN);

                        case MINUS:
                            //
                            // ':=-'
                            //
                            return this.push(Operation.OperationType.SUB_ASSIGN);

                        case MUL:
                            //
                            // ':=*'
                            //
                            return this.push(Operation.OperationType.MUL_ASSIGN);

                        case DIV:
                            //
                            // ':=/'
                            //
                            return this.push(Operation.OperationType.DIV_ASSIGN);

                        case MOD:
                            //
                            // ':=%'
                            //
                            return this.push(Operation.OperationType.MOD_ASSIGN);

                        case OR:
                            //
                            // ':=|'
                            //
                            return this.push(Operation.OperationType.OR_ASSIGN);

                        case AND:
                            //
                            // ':=&'
                            //
                            return this.push(Operation.OperationType.AND_ASSIGN);

                        case XOR:
                            //
                            // ':=^'
                            //
                            return this.push(Operation.OperationType.XOR_ASSIGN);

                        case SHOUT:
                            //
                            // ':=!' ist schwachsinn
                            //
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_SHOUT_ASSIGNMENT);

                        default:
                            //
                            // das war jetzt eine einfache ':='
                            //
                            this.stream().toSnapshot();
                            return this.push(Operation.OperationType.ASSIGN);
                    }
                }
                else
                {
                    //
                    // das sollte jetzt eine variablendeklaration werden
                    //
                    this.stream().toSnapshot();

                    if(this.topExpression().accept(new NodeDispatcher<Boolean>()
                        {
                            public NodeDispatcherResult<Boolean> visit(final ExpressionNodeIdentifier node) throws Exception
                            {
                                return new NodeDispatcherResult<Boolean>(true);
                            }

                        }) == null)
                    {
                        throw new ParserException(ParserException.ExceptionType.EXPRESSION_VARIABLE_NO_IDENTIFIER);
                    }
                    else
                    {
                        this.push(Operation.OperationType.VARIABLE);
                        return Expressions.Type.EXPRESSION;
                    }
                }

            default:
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_UNKNOWN_LAST_TYPE);

        }
    }

    //
    // verarbeite '&', '|''
    //
    private Type checkAndOrXor(final String expression, final Type type) throws Exception
    {
        switch(type)
        {
            case NONE:
            case OPERATION:
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_NO_OPERATION);

            case EXPRESSION:
            case BRACKET:
                //
                // hier wird noch geprüft, ob es sich um eine kombinierte
                // Zuweisung
                // handeln könnte
                //
                switch(this.stream().check4Next())
                {
                    case COLON:
                        //
                        // möglich wäre es
                        //
                        if(!this.stream().check4Next().equals(EQUAL))
                        {
                            //
                            // '&:' oder '|:' oder '^%:' sind dummes zeug
                            //
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_EQUAL_IN_COMBINED_ASSIGNMENT);
                        }
                        else
                        {
                            //
                            // jetzt passt es
                            //
                            switch(expression)
                            {
                                case AND:
                                    //
                                    // ein '&:='
                                    //
                                    return this.push(Operation.OperationType.AND_ASSIGN);

                                case OR:
                                    //
                                    // ein '|:='
                                    //
                                    return this.push(Operation.OperationType.OR_ASSIGN);

                                case XOR:
                                    //
                                    // ein '^:='
                                    //
                                    return this.push(Operation.OperationType.XOR_ASSIGN);
                            }
                        }

                    case AND:
                        if(expression.equals(AND))
                        {
                            //
                            // '&&'
                            //
                            return this.push(Operation.OperationType.AND);
                        }
                        else
                        {
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_OPERATOR_MIXMAX);
                        }

                    case OR:
                        if(expression.equals(OR))
                        {
                            //
                            // '||'
                            //
                            this.push(Operation.OperationType.OR);
                        }
                        else
                        {
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_OPERATOR_MIXMAX);
                        }

                    case XOR:
                        if(expression.equals(XOR))
                        {
                            //
                            // '^^'
                            //
                            this.push(Operation.OperationType.XOR);
                        }
                        else
                        {
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_OPERATOR_MIXMAX);
                        }

                    default:
                        //
                        // zweites zeichen zurücklegen und den ursprung
                        // auswerten
                        //
                        this.stream().toSnapshot();

                        switch(expression)
                        {
                            case MUL:
                                return this.push(Operation.OperationType.MUL);

                            case MOD:
                                return this.push(Operation.OperationType.MOD);

                            case DIV:
                                return this.push(Operation.OperationType.DIV);

                        }
                }

                return Expressions.Type.INVALID;

            default:
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_UNKNOWN_LAST_TYPE);
        }
    }

    //
    // prüfe, ob es bezeichner ist
    //
    private Type handleIdentifier(final String expression, final Type type) throws Exception
    {
        switch(type)
        {
            case NONE:
            case OPERATION:
                return this.push(new ExpressionNodeIdentifier(expression, (expression.equals(TRUE) || expression.equals(FALSE)) ? ExpressionType.Type.BOOL : ExpressionType.Type.STRING));

            case BRACKET:
                //
                // das wird mit Sicherheit ein cast
                //
                this.push(Operation.OperationType.CAST);

            case EXPRESSION:
                if(expression.equals(TRUE) || expression.equals(FALSE))
                {
                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_INVALID_BOOL);
                }
                else
                {
                    return this.push(new ExpressionNodeIdentifier(expression, ExpressionType.Type.STRING));
                }

            default:
                return Expressions.Type.INVALID;
        }
    }

    //
    // 'switch'
    //
    private final static String SWITCH = "switch";

    private Type handleSWITCH(final String expression, final Type type) throws Exception
    {
        if(!expression.equals(SWITCH))
        {
            return handleIdentifier(expression, type);
        }
        else
        {
            //
            // nach dem 'switch' muss ein klammerausdruck erfolgen
            //
            if(!this.nextBlancStreamResult().equals(BRACKET_LEFT))
            {
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_SWITCH_WITHOUT_COMPARISION);
            }
            else
            {
                this.checkRoundBracketLeft('\0', Type.NONE);

                while(true)
                {
                    //
                    // danach folgen die Zweige inkl. Block
                    //
                    if(this.nextBlancStreamResult().equals(EDGE_BRACKET_LEFT))
                    {
                        this.stream().consumeBlancs();
                        this.checkEdgeBracketLeft(Type.NONE);

                        if(this.nextBlancStreamResult().equals(CURLY_BRACKET_LEFT))
                        {
                            this.checkCurlyBracketLeft(Type.EXPRESSION);
                        }
                        else
                        {
                            throw new ParserException(ParserException.ExceptionType.EXPRESSION_SWITCH_WITHOUT_BLOCK);
                        }
                    }
                    else
                    {
                        this.stream().toSnapshot();
                        break;
                    }
                }

                NodeDispatcher<Boolean> checker = (new NodeDispatcher<Boolean>()
                    {
                        private Expressions          myExpressions          = null;

                        private Node                 myAnyBody              = null;

                        private ExpressionNodeSwitch myExpressionNodeSwitch = null;

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketRoundRight node) throws Exception
                        {
                            return (NodeDispatcherResult<Boolean>) node.child().accept(this);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketCurlyRight node) throws Exception
                        {
                            this.myAnyBody = node;
                            return (NodeDispatcherResult<Boolean>) this.myExpressions.popExpression().accept(this);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketEdgeRight node) throws Exception
                        {
                            return (NodeDispatcherResult<Boolean>) node.child().accept(this);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeStepListByComma node) throws Exception
                        {
                            this.myExpressionNodeSwitch.conditionNode(node.at(0));
                            this.myExpressions.push(this.myExpressionNodeSwitch);
                            return null;
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeStepListBySemicolon node) throws Exception
                        {
                            this.myExpressionNodeSwitch.append(ExpressionNodeJumpByArg.create(node.at(0), this.myAnyBody));
                            return null;
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeSwitch node) throws Exception
                        {
                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcher<Boolean> init(final Expressions expressions) throws Exception
                        {
                            this.myExpressions = expressions;
                            this.myExpressionNodeSwitch = ExpressionNodeSwitch.create(this.myExpressions);
                            return this;
                        }

                    }).init(this);

                while(true)
                {
                    final Node top = this.popExpression();

                    if(top == null)
                    {
                        throw new ParserException(ParserException.ExceptionType.EXPRESSION_SWITCH_INVALID);
                    }
                    else
                    {
                        if(top.accept(checker) != null)
                        {
                            this.push(top);
                            break;
                        }
                    }
                }

                return this.push(Operation.OperationType.SEMICOLON);
            }
        }
    }

    //
    // 'compare'
    //
    private final static String COMPARE = "compare";

    private Type handleCOMPARE(final String expression, final Type type) throws Exception
    {
        if(!expression.equals(COMPARE))
        {
            return handleSWITCH(expression, type);
        }
        else
        {
            //
            // nach dem 'compare' muss ein klammerausdruck erfolgen
            //
            if(!this.nextBlancStreamResult().equals(BRACKET_LEFT))
            {
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_WITHOUT_COMPARISION);
            }
            else
            {
                this.checkRoundBracketLeft(';', Type.NONE);

                for(
                        int checks = 1;
                        checks <= 3;
                        ++checks)
                {
                    //
                    // danach folgen die Zweige inkl. Block
                    //
                    if(this.nextBlancStreamResult().equals(EDGE_BRACKET_LEFT))
                    {
                        this.stream().consumeBlancs();
                        this.checkEdgeBracketLeft(Type.NONE);

                        if(this.nextBlancStreamResult().equals(CURLY_BRACKET_LEFT))
                        {
                            this.checkCurlyBracketLeft(Type.EXPRESSION);
                        }
                        else
                        {
                            switch(checks)
                            {
                                case 1:
                                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_WITHOUT_BLOCK1);

                                case 2:
                                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_WITHOUT_BLOCK2);

                                case 3:
                                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_WITHOUT_BLOCK3);

                            }
                        }
                    }
                    else
                    {
                        switch(checks)
                        {
                            case 1:
                                throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_WITHOUT_CHECK1);

                            case 2:
                                throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_WITHOUT_CHECK2);

                            case 3:
                                throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_WITHOUT_CHECK3);

                        }
                    }
                }

                NodeDispatcher<Boolean> checker = (new NodeDispatcher<Boolean>()
                    {
                        private Node        myLessNode    = null;

                        private Node        myIdentNode   = null;

                        private Node        myGreaterNode = null;

                        private Expressions myExpressions = null;

                        private Node        myAnyBody     = null;

                        private NodeDispatcherResult<Boolean> create() throws Exception
                        {
                            if(this.myLessNode != null)
                            {
                                if(this.myIdentNode != null)
                                {
                                    if(this.myGreaterNode != null)
                                    {
                                        //
                                        // nun sollte der vergleich in einer
                                        // runden klammer auf dem stack
                                        // liegen und ausgewertet werden
                                        // können
                                        //
                                        return (NodeDispatcherResult<Boolean>) this.myExpressions.popExpression().accept(this);
                                    }
                                }
                            }

                            return new NodeDispatcherResult<Boolean>(true);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketRoundRight node) throws Exception
                        {
                            //
                            // nach dem Compare werden die beiden
                            // Vergleichselemente in einer steplist
                            // aufgeführt
                            //
                            return (NodeDispatcherResult<Boolean>) node.child().accept(this);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeStepListBySemicolon node) throws Exception
                        {
                            //
                            // jetzt ist alles vorhanden und der Ausdruck
                            // kann erstellt werden
                            //
                            if(node.size() != 2)
                            {
                                throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_WRONG_ARGS);
                            }
                            else
                            {
                                this.myExpressions.push(ExpressionNodeCompare.create(node, this.myLessNode, this.myIdentNode, this.myGreaterNode));
                                return new NodeDispatcherResult<Boolean>(true);
                            }
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketCurlyRight node) throws Exception
                        {
                            //
                            // zunächst liegt irgendein körperelement auf
                            // dem stack das nächste stackelement bestimmt,
                            // in welchen zweig der körper gehört
                            //
                            this.myAnyBody = node;
                            return (NodeDispatcherResult<Boolean>) this.myExpressions.popExpression().accept(this);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketEdgeRight node) throws Exception
                        {
                            //
                            // die bedingungen werden über den operator
                            // abgebildet, der in einer steplist unter einer
                            // eckigen klammer hängt
                            //
                            return (NodeDispatcherResult<Boolean>) node.child().accept(this);
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeGreater node) throws Exception
                        {
                            //
                            // jetzt wird der körper einem zweig zugeordnet.
                            // sind alle zweige gesetzt, wo kann der
                            // ausdruck erstellt werden
                            //
                            if(this.myGreaterNode == null)
                            {
                                this.myGreaterNode = this.myAnyBody;
                                return this.create();
                            }
                            else
                            {
                                throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_DOUBLE_GREATER);
                            }
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeLess node) throws Exception
                        {
                            //
                            // jetzt wird der körper einem zweig zugeordnet.
                            // sind alle zweige gesetzt, wo kann der
                            // ausdruck erstellt werden
                            //
                            if(this.myLessNode == null)
                            {
                                this.myLessNode = this.myAnyBody;
                                return this.create();
                            }
                            else
                            {
                                throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_DOUBLE_LESS);
                            }
                        }

                        public NodeDispatcherResult<Boolean> visit(final ExpressionNodeEqual node) throws Exception
                        {
                            //
                            // jetzt wird der körper einem zweig zugeordnet.
                            // sind alle zweige gesetzt, wo kann der
                            // ausdruck erstellt werden
                            //
                            if(this.myIdentNode == null)
                            {
                                this.myIdentNode = this.myAnyBody;
                                return this.create();
                            }
                            else
                            {
                                throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_DOUBLE_EQ);
                            }
                        }

                        public NodeDispatcher<Boolean> init(final Expressions expressions)
                        {
                            this.myExpressions = expressions;
                            return this;
                        }

                    }).init(this);

                for(
                        int checks = 1;
                        checks <= 3;
                        ++checks)
                {
                    if(this.popExpression().accept(checker) == null)
                        throw new ParserException(ParserException.ExceptionType.EXPRESSION_COMPARE_INVALID);
                }

                switch(type)
                {
                    case NONE:
                    case EXPRESSION:
                        return this.push(Operation.OperationType.SEMICOLON);

                    case OPERATION:
                        return Expressions.Type.EXPRESSION;

                    default:
                        return Expressions.Type.INVALID;

                }
            }
        }
    }

    //
    // 'for'
    //
    private final static String FOR = "for";

    private Type handleFOR(final String expression, final Type type) throws Exception
    {
        if(!expression.equals(FOR))
        {
            return handleCOMPARE(expression, type);
        }
        else
        {
            //
            // nach dem 'for' muss ein klammerausdruck folgen
            //
            if(!this.nextBlancStreamResult().equals(BRACKET_LEFT))
            {
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_FOR_WITHOUT_LOOP_DECL);
            }
            else
            {
                this.checkRoundBracketLeft(';', Type.NONE);

                //
                // danach muss der Block folgen
                //
                if(!this.nextBlancStreamResult().equals(CURLY_BRACKET_LEFT))
                {
                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_FOR_BODY);
                }
                else
                {
                    this.checkCurlyBracketLeft(Type.EXPRESSION);

                    if(this.popExpression().accept((new NodeDispatcher<Boolean>()
                        {
                            private Expressions myExpressions = null;

                            private Node        myBodyNode    = null;

                            public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketRoundRight node) throws Exception
                            {
                                //
                                // innerhalb der runden klammer sind die
                                // start- und endebedingung und die
                                // schleifekomponente aufgeführt
                                // alles ist in einer steplist mit drei
                                // elementen hinterlegt
                                //
                                return (NodeDispatcherResult<Boolean>) node.child().accept(this);
                            }

                            public NodeDispatcherResult<Boolean> visit(final ExpressionNodeStepListBySemicolon node) throws Exception
                            {
                                if(node.size() != 3)
                                {
                                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_FOR_WRONG_ARGS);
                                }
                                else
                                {
                                    //
                                    // jetzt sind alle drei komponenten
                                    // vorhanden und auch der body
                                    // der ausdruck kann jetzt erstellt
                                    // werden
                                    //
                                    final ExpressionNodeStepListBySemicolon stepList = new ExpressionNodeStepListBySemicolon();

                                    stepList.append(ExpressionNodeFor.create(node.at(2), node.at(1), node.at(0), this.myBodyNode));
                                    this.myExpressions.push(ExpressionNodeBracketCurlyRight.create(stepList));
                                    return new NodeDispatcherResult<Boolean>(true);
                                }
                            }

                            public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketCurlyRight node) throws Exception
                            {
                                this.myBodyNode = node;
                                return (NodeDispatcherResult<Boolean>) this.myExpressions.popExpression().accept(this);
                            }

                            public NodeDispatcher<Boolean> init(final Expressions expressions)
                            {
                                this.myExpressions = expressions;
                                return this;
                            }

                        }).init(this)) == null)
                    {
                        throw new ParserException(ParserException.ExceptionType.EXPRESSION_FOR_INVALID_CONSTRUCTION);
                    }
                    else
                    {
                        switch(type)
                        {
                            case NONE:
                            case EXPRESSION:
                                return this.push(Operation.OperationType.SEMICOLON);

                            case OPERATION:
                                return Expressions.Type.EXPRESSION;

                            default:
                                return Expressions.Type.INVALID;
                        }
                    }
                }
            }
        }
    }

    //
    // 'else'
    //
    private final static String ELSE = "else";

    private Type handleELSE(final Type type) throws Exception
    {
        //
        // gibt es einen 'else'-zweig ?
        //
        if(this.nextBlancStreamResult().equals(ELSE))
        {
            if(!this.nextBlancStreamResult().equals(CURLY_BRACKET_LEFT))
            {
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_IF_WITHOUT_ELSE);
            }
            else
            {
                this.checkCurlyBracketLeft(Type.EXPRESSION);
            }
        }
        else
        {
            //
            // wenn nicht, wird ein Dummy-Else angelegt
            //
            this.stream().toSnapshot();
            this.push(ExpressionNodeBracketCurlyRight.create(new ExpressionNodeStepListBySemicolon()));
            this.stream().toSnapshot();
        }

        //
        // jetzt sind alle teile ausgewertet
        //
        if(this.popExpression().accept((new NodeDispatcher<Boolean>()
            {
                private Expressions myExpressions = null;

                private Node        myThenNode    = null;

                private Node        myElseNode    = null;

                public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketRoundRight node) throws Exception
                {
                    //
                    // der bedingungsausdruck ist in einer runden klammer,
                    // die wiederum eine steplist enthält mit (hoffentlich
                    // nur) einem element
                    //
                    return (NodeDispatcherResult<Boolean>) node.child().accept(this);
                }

                public NodeDispatcherResult<Boolean> visit(final ExpressionNodeStepListByComma node) throws Exception
                {
                    if(node.size() != 1)
                    {
                        throw new ParserException(ParserException.ExceptionType.EXPRESSION_IF_INVALID_CONSTRUCTION);
                    }
                    else
                    {
                        //
                        // jetzt liegen bedingung, und beide zweige vor.
                        // der ausdruck kann jetzt erstellt werden
                        //
                        this.myExpressions.push(ExpressionNodeIf.create(node.at(0), this.myThenNode, this.myElseNode));
                        return new NodeDispatcherResult<Boolean>(true);
                    }
                }

                public NodeDispatcherResult<Boolean> visit(final ExpressionNodeBracketCurlyRight node) throws Exception
                {
                    //
                    // diese methode wird zweimal durchlaufen. einmal für
                    // den then-teil und für den else-teil (der ggf. als
                    // leerer dummy angelegt ist
                    //
                    if(this.myElseNode == null)
                    {
                        //
                        // danach sollte der bedingungsausdruck innerhalb
                        // einer runden klammer ausgewertet werden
                        //
                        this.myElseNode = node;
                        return (NodeDispatcherResult<Boolean>) this.myExpressions.popExpression().accept(this);
                    }
                    else
                    {
                        if(this.myThenNode == null)
                        {
                            //
                            // nach dem then sollte diese methode rekursiv
                            // über den dispatcher noch einmal verarbeitet
                            // werden
                            //
                            this.myThenNode = node;
                            return (NodeDispatcherResult<Boolean>) this.myExpressions.popExpression().accept(this);
                        }
                        else
                        {
                            return null;
                        }
                    }
                }

                public NodeDispatcher<Boolean> init(final Expressions expressions)
                {
                    this.myExpressions = expressions;
                    return this;
                }

            }).init(this)) == null)
        {
            throw new ParserException(ParserException.ExceptionType.EXPRESSION_IF_INVALID_CONSTRUCTION);
        }
        else
        {
            switch(type)
            {
                case NONE:
                case EXPRESSION:
                case OPERATION:
                    return this.push(Operation.OperationType.SEMICOLON);

                default:
                    return Expressions.Type.INVALID;
            }
        }
    }

    //
    // 'if'
    //
    private final static String IF = "if";

    private Type handleIF(final String expression, final Type type) throws Exception
    {
        if(!expression.equals(IF))
        {
            return this.handleFOR(expression, type);
        }
        else
        {
            //
            // nach einem 'if' muss ein Klammerausdruck folgen
            //
            if(!this.nextBlancStreamResult().equals(BRACKET_LEFT))
            {
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_IF_WITHOUT_CONDITION);
            }
            else
            {
                this.checkRoundBracketLeft('\0', Type.NONE);

                //
                // prüfe, ob es einen 'then'-zweig gibt
                //
                if(this.nextBlancStreamResult().equals(CURLY_BRACKET_LEFT))
                {
                    this.checkCurlyBracketLeft(Type.EXPRESSION);
                    return this.handleELSE(type);
                }
                else
                {
                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_IF_WITHOUT_THEN);
                }
            }
        }
    }

    //
    // prüfe, ob ein Ausdruck ausschließlich numerisch ist
    //
    private String checkNumerical(final String expression) throws ParserException
    {
        for(
                int chars = 0;
                chars < expression.length();
                ++chars)
        {
            final char theChar = expression.charAt(chars);

            if((theChar >= '0') &&
                    (theChar <= '9'))
            {
                ;
            }
            else
            {
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_NOT_NUMERICAL);
            }
        }

        return expression;
    }

    //
    // prüfe ggf. auf exponenten
    // verarbeite einen numerischen wert
    //
    private Type handleNumerical(final String expression, final ExpressionType.Type type) throws Exception
    {
        int index = expression.indexOf(LOWER_EXP);

        if(index < 0)
        {
            index = expression.indexOf(UPPER_EXP);
        }

        if(index < 0)
        {
            //
            // das ist jetzt nur ein int
            //
            return this.push(new ExpressionNodeIdentifier(expression, type));
        }
        else
        {
            //
            // da ein exponent gefunden wurde, muss
            // der rest jetzt irgendwie zusammen passen
            //
            final String left = expression.substring(0, index);
            final String right = (index == expression.length()) ? null : expression.substring(index + 1);

            if(right != null)
            {
                //
                // der bezeichner endete mit einem 'E' oder 'e'.
                // kommt danach ein vorzeichen ?
                //
                switch(right.substring(0, 1))
                {
                    case PLUS:
                        return this.push(new ExpressionNodeIdentifier(this.checkNumerical(left).concat(UPPER_EXP).concat(PLUS.concat(this.checkNumerical(right.substring(1)))), type));

                    case MINUS:
                        return this.push(new ExpressionNodeIdentifier(this.checkNumerical(left).concat(UPPER_EXP).concat(MINUS.concat(this.checkNumerical(right.substring(1)))), type));

                    default:
                        //
                        // mantisse ohne exponent. Macht nix
                        //
                        this.stream().toSnapshot();
                        return this.push(new ExpressionNodeIdentifier(this.checkNumerical(left).concat(UPPER_EXP).concat(PLUS.concat(this.checkNumerical(right))), type));

                }
            }
            else
            {
                //
                // das sieht jetzt seeeehr seltsam aus
                //
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_MISSING_EXPONENT);
            }
        }
    }

    private Type checkIdentifier(final String expression, final Type type) throws Exception
    {
        final char first = expression.charAt(0);

        if((first == '\'') || (first == '"'))
        {
            //
            // zeichenketten werden direkt übertragen
            //
            return this.push(new ExpressionNodeIdentifier(expression, ExpressionType.Type.STRING));
        }
        else
        {
            if(((first >= 'A') && (first <= 'Z')) || ((first >= 'a') && (first <= 'z')) || (first == '_'))
            {
                //
                // alphabezeichner
                //
                return this.handleIF(expression, type);
            }
            else
            {
                //
                // das ist jetzt etwas numerisches.
                // ist ein ',' vorhanden ?
                //
                if(this.stream().check4Next().equals(DOT))
                {
                    //
                    // yepp, also wird der rest auch angeklebt
                    //
                    return this.handleNumerical(this.checkNumerical(expression).concat(DOT.concat(this.checkNumerical(this.stream().check4Next()))), ExpressionType.Type.DOUBLE);
                }
                else
                {
                    //
                    // könnte jetzt aber immer noch exponenten beinhalten
                    //
                    this.stream().toSnapshot();
                    return this.handleNumerical(this.checkNumerical(expression), ExpressionType.Type.LONG);
                }
            }
        }
    }

    //
    // ein ','
    //
    private Type checkComma(final Type type) throws Exception
    {
        switch(type)
        {
            case OPERATION:
                //
                // die letzte Eingabe kann keine Operation gewesen sein
                //
                throw new ParserException(ParserException.ExceptionType.EXPRESSION_OPERATION_BEFORE_COMMA);

            case NONE:
                //
                // ein Komma am Anfang ?
                // würde zu einer Exception im Nachfolgenden führen
                //

            case EXPRESSION:
                return this.push(Operation.OperationType.COMMA);

            default:
                return Expressions.Type.INVALID;
        }
    }

    //
    // ein ';'
    //
    private Type checkSemicolon(final Type type) throws Exception
    {
        return this.push(Operation.OperationType.SEMICOLON);
    }

    //
    // prüfe die Ausdrücke durch
    //
    private Type tryAnything(final String expression, final Type type) throws Exception
    {
        switch(expression)
        {
            case BRACKET_LEFT:
                return this.checkRoundBracketLeft(',', type);

            case BRACKET_RIGHT:
                return this.checkBracketRoundRight(type);

            case CURLY_BRACKET_LEFT:
                return this.checkCurlyBracketLeft(type);

            case CURLY_BRACKET_RIGHT:
                return this.checkBracketCurlyRight(type);

            case EDGE_BRACKET_LEFT:
                return this.checkEdgeBracketLeft(type);

            case EDGE_BRACKET_RIGHT:
                return this.checkBracketEdgeRight(type);

            case POINTER:
                return this.checkPointer(type);

            case PLUS:
            case MINUS:
                return this.checkPlusMinus(expression, type);

            case MUL:
            case MOD:
            case DIV:
                return this.checkMulDiv(expression, type);

            case LESS:
            case GREATER:
            case SHOUT:
            case EQUAL:
                return this.checkComparision(expression, type);

            case COLON:
                return this.checkAssignment(type);

            case AND:
            case OR:
                return this.checkAndOrXor(expression, type);

            case COMMA:
                if(this.validDelimeter() == ',')
                    return this.checkComma(type);
                else
                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_INVALID_DELIMETER);

            case SEMICOLON:
                if(this.validDelimeter() == ';')
                    return this.checkSemicolon(type);
                else
                    throw new ParserException(ParserException.ExceptionType.EXPRESSION_INVALID_DELIMETER);

            default:
                return this.checkIdentifier(expression, type);
        }
    }

    public Expressions(final SourceStream stream)
    {
        this.myStream = stream;
    }

    private Expressions(final SourceStream stream, final Operation.OperationType operation) throws Exception
    {
        this(stream);
        this.push(operation);
    }

    public final Node process() throws Exception
    {
        return this.process(';');
    }

    public final Node process(final char validDelimeter) throws Exception
    {
        this.validDelimeter(validDelimeter);

        Type theType = Type.NONE;

        while(true)
        {
            this.stream().consumeBlancs();

            SourceStream.StreamResult streamResult = this.stream().getNext();

            if(streamResult.stepAction() == SourceStream.StepAction.END_REACHED)
            {
                break;
            }
            else
            {
                theType = this.tryAnything(streamResult.result(), theType);

                if(theType == Type.TERMINATE)
                {
                    break;
                }
            }
        }

        while(this.operations() > 0)
        {
            this.popOperation().function(this);
        }

        if(this.expressions() == 1)
        {
            return this.popExpression();
        }
        else
        {
            if(this.expressions() == 0)
            {
                throw new ParserException(ParserException.ExceptionType.UNABLE2CREATE_WITHOUT_EXPRESSION);
            }
            else
            {
                throw new ParserException(ParserException.ExceptionType.UNABLE2CREATE_EXPRESSION_TREE_TOO_MANY_ELEMENTS);
            }
        }
    }

}
