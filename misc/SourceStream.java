package misc;

import java.util.Vector;

import exception.ParserException;

public class SourceStream
{
    public enum StepAction
    {
        NONE,
        END_REACHED,
        LINEFEED,
        SUCCESS;
    }

    public final static class StreamResult
    {
        private StepAction myStepAction = StepAction.NONE;

        public final StepAction stepAction()
        {
            return this.myStepAction;
        }

        final void stepAction(final StepAction stepAction)
        {
            this.myStepAction = stepAction;
        }

        private String myResult = null;

        public final String result()
        {
            return this.myResult;
        }

        final void result(final String result)
        {
            this.myResult = result;
        }

        private StreamResult()
        {
        }

        private static StreamResult instance = null;

        static StreamResult getInstance()
        {
            if(instance == null)
                instance = new StreamResult();

            return instance;
        }
    }

    private final StreamResult myResult      = StreamResult.getInstance();

    private Vector<String>     mySourcelines = new Vector<String>();

    private int                myCurrentLine = 0;

    public final int currentLine()
    {
        return this.myCurrentLine;
    }

    private int myCurrentColumn = 0;

    public final int currentColumn()
    {
        return this.myCurrentColumn;
    }

    private int    mySnapshotColumn = 0;

    private int    mySnapshotLine   = 0;

    private char   myInString       = '\0';

    private String myCurrentString  = null;

    //
    // schiebe in die nächste zeile vor, die inhalt
    // enthält
    //
    public final StepAction nextLine()
    {
        this.myCurrentLine++;
        this.myCurrentColumn = 0;

        if(this.myCurrentLine >= this.mySourcelines.size())
        {
            return StepAction.END_REACHED;
        }
        else
        {
            this.myCurrentString = this.mySourcelines.elementAt(this.myCurrentLine);

            return (this.myCurrentString.length() == 0) ? this.nextLine() : StepAction.LINEFEED;
        }
    }

    //
    // bestimme das nächste zeichen in dem datenstrom
    //
    private final StepAction toNextChar()
    {
        this.myCurrentColumn++;

        if(this.myCurrentColumn >= myCurrentString.length())
        {
            return this.nextLine();
        }
        else
        {
            return StepAction.SUCCESS;
        }
    }

    private final static String EMPTY_STRING = new String();

    public final static String  BLANC        = " ";

    public final static String  TAB          = "\t";

    public final static String  CRLF         = "\n";

    //
    // überspringe alle blancs, CR und tabulatoren
    //
    public final StepAction consumeBlancs() throws Exception
    {
        if(this.myCurrentLine >= this.mySourcelines.size())
        {
            return StepAction.END_REACHED;
        }
        else
        {
            while(true)
            {
                switch(this.myCurrentString.charAt(myCurrentColumn))
                {
                    case ' ':
                    case '\t':
                        //
                        // blanc oder tabulator
                        //
                        switch(this.toNextChar())
                        {
                            case END_REACHED:
                                //
                                // hier ist schluss mit der verarbeitung
                                //
                                return StepAction.END_REACHED;

                            case NONE:
                            case LINEFEED:
                            case SUCCESS:
                                //
                                // weitermachen
                                //

                            default:
                                ;

                        }
                        break;

                    case '°':
                        //
                        // normales dateiende
                        //
                        return StepAction.END_REACHED;

                    default:
                        //
                        // alle blanks aufgeschmatzt
                        //
                        return StepAction.SUCCESS;
                }
            }
        }
    }

    public final StreamResult getNext() throws Exception
    {
        if(this.myCurrentLine >= this.mySourcelines.size())
        {
            this.myResult.stepAction(StepAction.END_REACHED);
            return this.myResult;
        }
        else
        {
            this.myResult.stepAction(StepAction.SUCCESS);
            this.mySnapshotColumn = this.myCurrentColumn;
            this.mySnapshotLine = this.myCurrentLine;

            String result = EMPTY_STRING;

            while(true)
            {
                final char theChar = this.myCurrentString.charAt(myCurrentColumn);

                if(this.myInString != '\0')
                {
                    //
                    // wir befinden uns in einer zeichenkette
                    //
                    if(this.myInString == theChar)
                    {
                        //
                        // ende einer zeichenkette
                        //
                        this.myInString = '\0';
                        this.myResult.result(theChar + result + theChar);
                        this.toNextChar();
                        return this.myResult;
                    }
                    else
                    {
                        //
                        // eine zeichenkette sammelt (fast) alles zusammen
                        //
                        if(theChar == '°')
                        {
                            //
                            // dateiende im String ist unzulässig
                            //
                            throw new ParserException(ParserException.ExceptionType.INVALID_EOF_IN_STRING);
                        }
                        else
                        {
                            switch(this.toNextChar())
                            {
                                case LINEFEED:
                                    //
                                    // zeilenvorschub in strings ist unzulässig
                                    //
                                    throw new ParserException(ParserException.ExceptionType.STRING_EXCEEDS_LINE);

                                case END_REACHED:
                                    //
                                    // dateiende im String ist unzulässig
                                    //
                                    throw new ParserException(ParserException.ExceptionType.INVALID_EOF_IN_STRING);

                                default:
                                    //
                                    // ansonsten geht es in der schleife weiter
                                    //
                                    result += theChar;

                            }
                        }
                    }
                }
                else
                {
                    switch(theChar)
                    {
                        case '"':
                        case '\'':
                            //
                            // anfang einer zeichenkette schließt
                            // bestehendes wort ab und gibt dieses zurück
                            // (sofern vorhanden) ansonsten wird eine
                            // zeichenkette aufgebaut
                            //
                            if(result.length() != 0)
                            {
                                this.myResult.result(result);
                                return this.myResult;
                            }
                            else
                            {
                                this.myInString = theChar;
                                switch(this.toNextChar())
                                {
                                    case LINEFEED:
                                        //
                                        // zeilenvorschub in strings ist
                                        // unzulässig
                                        //
                                        throw new ParserException(ParserException.ExceptionType.STRING_EXCEEDS_LINE);

                                    case END_REACHED:
                                        //
                                        // dateiende im String ist unzulässig
                                        //
                                        throw new ParserException(ParserException.ExceptionType.INVALID_EOF_IN_STRING);

                                    default:
                                        //
                                        // der rest wird jetzt über die schleife
                                        // eingelesen
                                        //
                                        ;

                                }
                            }

                            break;

                        case '\t':
                        case ' ':
                            //
                            // blanc oder tabulator schließt ein bestehendes
                            // wort ab und gibt dieses zurück (sofern
                            // vorhanden)
                            //
                            if(result.length() != 0)
                            {
                                this.myResult.result(result);
                                return this.myResult;
                            }
                            else
                            {
                                //
                                // ansammlungen von blancs und/oder tabulatoren
                                // werden zusammengefassst
                                //
                                result = BLANC;
                                this.consumeBlancs();
                                break;
                            }

                        case '!':
                        case '§':
                        case '$':
                        case '%':
                        case '&':
                        case '/':
                        case '(':
                        case ')':
                        case '=':
                        case '?':
                        case '{':
                        case '}':
                        case '[':
                        case ']':
                        case '\\':
                        case '*':
                        case '+':
                        case '~':
                        case '#':
                        case '<':
                        case '>':
                        case '|':
                        case ';':
                        case ',':
                        case ':':
                        case '.':
                        case '-':
                        case '^':
                            //
                            // irgendwelche sonderzeichen
                            //
                            if(result.length() == 0)
                            {
                                //
                                // das einzelne zeichen wird als
                                // rückgabeergebnis definiert
                                //
                                result = String.valueOf(theChar);
                                this.toNextChar();
                            }
                            //
                            // ansonsten wird das bisher eingelesene
                            // zurückgegeben und das zeichen wird noch einmal
                            // gelesen
                            //
                            this.myResult.result(result);
                            return this.myResult;

                        case '°':
                            this.myResult.stepAction(StepAction.END_REACHED);
                            return this.myResult;

                        default:
                            //
                            // ziffer, alpha oder underscore werden einfach
                            // zusammengeklatscht
                            //
                            if(((theChar >= 'A') && (theChar <= 'Z')) || ((theChar >= 'a') && (theChar <= 'z')) || ((theChar >= '0') && (theChar <= '9')) || (theChar == '_'))
                            {
                                if(result.length() > 0)
                                {
                                    if(result == BLANC)
                                    {
                                        //
                                        // bisherige blancs werden zurückgegeben
                                        // und das zeichen wird noch einmal
                                        // eingelesen
                                        //
                                        this.myResult.result(result);
                                        return this.myResult;
                                    }
                                }

                                //
                                // alles andere wird einfach zusammengeklatscht
                                //
                                result += theChar;
                                switch(this.toNextChar())
                                {
                                    case LINEFEED:
                                    case END_REACHED:
                                        //
                                        // zeilenvorschub oder dateiende liefern
                                        // das Ergebnis zurück
                                        //
                                        this.myResult.result(result);
                                        return this.myResult;

                                    default:
                                        //
                                        // der rest wird weitergelesen
                                        //
                                }
                            }
                            else
                            {
                                throw new ParserException(ParserException.ExceptionType.UNKNOWN_CHARACTER, EMPTY_STRING + theChar);
                            }
                    }
                }
            }
        }
    }

    private static SourceStream instance = null;

    public static SourceStream getInstance()
    {
        if(instance == null)
        {
            instance = new SourceStream();
        }

        return instance;
    }

    private SourceStream()
    {
    }

    public final void addString(final String newLine)
    {
        this.mySourcelines.add(newLine);
    }

    public final void toSnapshot()
    {
        this.myCurrentColumn = this.mySnapshotColumn;
        this.myCurrentLine = this.mySnapshotLine;
        this.myCurrentString = this.mySourcelines.elementAt(this.myCurrentLine);
    }

    public final void reset()
    {
        this.myCurrentLine = this.myCurrentColumn = this.mySnapshotColumn = this.mySnapshotLine = 0;
        this.myInString = '\0';
        this.myCurrentString = this.mySourcelines.elementAt(this.myCurrentLine);
    }

    public final void clear()
    {
        this.mySourcelines.clear();
        this.myCurrentLine = this.myCurrentColumn = this.mySnapshotColumn = this.mySnapshotLine = 0;
        this.myInString = '\0';
        this.myCurrentString = null;
    }

    public final void print()
    {
        for(String line : this.mySourcelines)
        {
            System.out.println(line);
        }
    }

    public final String check4Next() throws Exception
    {
        SourceStream.StreamResult streamResult = this.getNext();

        if(streamResult.stepAction() == SourceStream.StepAction.END_REACHED)
        {
            throw new ParserException(ParserException.ExceptionType.INVALID_EOF);
        }
        else
        {
            return streamResult.result();
        }
    }
    
    public final String repeat()
    {
        return this.myResult.result();
    }

}
