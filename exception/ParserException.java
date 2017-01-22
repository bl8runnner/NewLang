/**
 * 
 */
package exception;

import misc.SourceStream;

/**
 * @author LU132BOD
 *
 */
@SuppressWarnings("serial")
public class ParserException extends Exception
{
    public enum ExceptionType
    {
        OK(
                "OK"),
        VALID_EOF(
                "Korrektes Dateiende"),
        INVALID_EOF_IN_STRING(
                "Dateiende im String"),
        UNKNOWN_CHARACTER(
                "Unbekanntes Zeichen : "),
        STRING_EXCEEDS_LINE(
                "Zeilenvorschub in Stringkonstanter"),
        INVALID_EOF(
                "Unzulässiges Dateiende erreicht "),

        INVALID_NODE(
                "Unzulässiger Kindnoten an dieser Stelle : "),

        NO_BLANCS_ALLOWED(
                "Freiräume sind nicht zulässig "),

        KEYWORD_NOR_ALLOWED(
                "Folgendes Schlüsselwort als Bezeichner nicht erlaubt: "),

        IDENTIFIER_NO_START_WITH_UPPERCASE(
                "Pfad-/Klassenangaben im 'class'/'namespace' müssen mit Großbuchstaben beginnen"),
        EMPTY_PATH(
                "Eine Pfadangabe darf nicht mit '::' eingeleitet werden"),
        NAMESPACE_EXPECTED(
                "'namespace' erwartet"),
        EMPTY_NAMESPACE(
                "'namespace' muss mit einem gültigen Pfad/Namespacenamen weitergeführt werden"),
        NAMESPACE_WRONG_TERMINATED(
                "'namespace' ist falsch abgeschlossen. Unzulässig : "),
        NAMESPACE_NO_START_WITH_UPPERCASE(
                "Pfadangaben im 'namespace' müssen mit Großbuchstaben beginnen"),
        NAMESPACE_NO_IDENTIFIER(
                "'namespace' ohne Identifier (. an letzter Stelle)"),
        NAMESPACE_NO_BRACKET_LEFT(
                "Nach dem Namespacepfad muss eine '{' aufgeführt werden"),
        NAMESPACE_NO_BRACKET_RIGHT(
                "Ein Namespace muss mit '}' abgeschlossen werden"),
        NAMESPACE_DOUBLED(
                "In einem Namespace wird ein weiterer Namespace mit gleichem Name definiert : "),

        CLASS_EXPECTED(
                "'class' erwartet"),
        EMPTY_CLASS(
                "'class' muss mit einem gültigen Pfad/Klassennamen weitergeführt werden"),
        CLASS_WRONG_TERMINATED(
                "'class' ist falsch abgeschlossen. Unzulässig : "),
        CLASS_WRONG_PATH_SEPARATOR(
                "Klassenpfade müssen mit '::' getrennt werden"),
        CLASS_NO_IDENTIFIER(
                "'class' ohne Identifier (. an letzter Stelle)"),
        CLASS_NAME_EXPECTED(
                "Ein Klassenbezeichner wird erwartet"),
        CLASS_DERIVED_OP_EXPECTED(
                "Die Vererbungssignatur '->' ist unvollständig"),
        CLASS_NO_BRACKET_LEFT(
                "Nach dem Klassennamen/der Vererbungssignatur muss eine '{' aufgeführt werden"),
        CLASS_NO_BRACKET_RIGHT(
                "Eine Klasse muss mit '}' abgeschlossen werden"),
        CLASS_DOUBLED(
                "In einem Namespace wird eine weitere Klasse mit gleichem Name definiert : "),
        CLASSNAME2NAMESPACE(
                "Der Klassenname darf nicht mit einem Teilnamen eines Namespace übereinstimmen : "),

        INVALID_MODIFIER(
                "Dieser Modifier ist an dieser Stelle unzulässig: "),
        DOUBLE_MODIFIER(
                "Doppelter Modifier: "),
        TOO_MUCH_MODIFIERS(
                "Zu viele Modifizierer: "),
        INVALID_MODIFIER_COMBINATION(
                "Die Kombination dieser Modifier ist an dieser Stelle unzulässig: "),

        EXPRESSION_INVALID_DELIMETER(
                "Fehlerhaftes Trennzeichen in Liste"),
        EXPRESSION_PANIC(
                "Grundlegender Fehler im Parsing ...."),
        EXPRESSION_NO_OPERATION(
                "Eine Mehrfachverknüpfung von Operationen ist unzulässig"),
        EXPRESSION_BAD_OPERATION(
                "Fehlerhafter Operator : "),
        EXPRESSION_WITHOUT_EXPONENT(
                "Der Exponent ist trotz 'e/E' nicht angeführt"),
        EXPRESSION_NOT_NUMERICAL(
                "Der numerische Ausdruck enthält nicht-numerische Zeichen"),
        EXPRESSION_NO_EXPRESSION(
                "Eine Mehrfachverknüpfung von Ausdrücken ist unzulässig"),
        EXPRESSION_SINGLE_EQUAL(
                "Ein einzelnes '=' ist unzulässig. Evtl. ein ':=' ?"),
        EXPRESSION_BLANC_IN_ASSIGNMENT(
                "Ein Leerzeichen in einer (kombinierten) Zuweisung ist nicht zulässig"),
        EXPRESSION_MISSING_EQUAL_IN_COMBINED_ASSIGNMENT(
                "In einer kombinierten Zuweisung fehlt das '='"),
        EXPRESSION_OPERATOR_MIXMAX(
                "Seltsame Kombination von Operationen"),
        EXPRESSION_MISSING_MATCHING_BRACKET(
                "Öffnende/schließende Klammer fehlt"),
        EXPRESSION_MISSING_EXPONENT(
                "Mantisse ohne Exponent."),
        EXPRESSION_MISSING_LEFT_OPERATOR(
                "Linker Operand fehlt"),
        EXPRESSION_MISSING_RIGHT_OPERATOR(
                "Rechter Operand fehlt"),
        EXPRESSION_MISSING_OPERATOR(
                "Operand fehlt"),
        EXPRESSION_MISSING_OPERANDS(
                "Operanden fehlen in Operation : "),
        EXPRESSION_INVALID_REFERENCES(
                "Referenzierung auf Element bzw. auf Instanz kann nicht aufgelöst werden, da fehlender Parameter"),
        EXPRESSION_PLUS_MINUS_INVALID(
                "Addition/Subtraktion an dieser Stelle unzulässig"),
        EXPRESSION_SHOUT_ASSIGNMENT(
                "Invertierung an dieser Stelle unzulässig"),
        EXPRESSION_OPERATION_BEFORE_COMMA(
                "Comma an dieser Stelle unzulässig (Rechenoperation)"),
        EXPRESSION_OPERATION_BEFORE_SEMICOLON(
                "Semicolon an dieser Stelle unzulässig (Rechenoperation)"),
        EXPRESSION_IF_WITHOUT_CONDITION(
                "Keine Bedingung nach 'if'. Klammern vergessen ?"),
        EXPRESSION_IF_WRONG_CONDITIONS(
                "Fehlerhafte Anzahl von Bedingungen im 'if'. Es ist nur ein zulässig."),
        EXPRESSION_IF_WITHOUT_THEN(
                "Fehlende '{' nach Bedingung"),
        EXPRESSION_IF_WITHOUT_ELSE(
                "Fehlende '{' nach 'else'"),
        EXPRESSION_IF_INVALID_CONSTRUCTION(
                "Das 'if' ist zweifelhaft erstellt"),
        EXPRESSION_INVALID_BOOL(
                "Unzulässiger Bool'scher Ausdruck an dieser Stelle"),
        EXPRESSION_MISSING_CONDITION_OPERATOR(
                "'if' ohne Bedingung"),
        EXPRESSION_MISSING_THEN_OPERATOR(
                "'if' ohne then-Zweig"),
        EXPRESSION_MISSING_FOR_BODY(
                "'for' ohne Body"),
        EXPRESSION_FOR_WITHOUT_LOOP_DECL(
                "Nach dem 'for' fehlt der Klammerausdruck"),
        EXPRESSION_FOR_WRONG_ARGS(
                "In dem 'for' ist die Anzahl der Argumente ungültig"),
        EXPRESSION_FOR_INVALID_CONSTRUCTION(
                "Das 'for' ist zweifelhaft erstellt"),
        EXPRESSION_COMPARE_WITHOUT_COMPARISION(
                "Nach dem 'compare' fehlt der Klammerausdruck"),
        EXPRESSION_COMPARE_WITHOUT_BLOCK1(
                "Nach dem 'compare' fehlt der 1. Block"),
        EXPRESSION_COMPARE_WITHOUT_BLOCK2(
                "Nach dem 'compare' fehlt der 2. Block"),
        EXPRESSION_COMPARE_WITHOUT_BLOCK3(
                "Nach dem 'compare' fehlt der 3. Block"),
        EXPRESSION_COMPARE_WITHOUT_CHECK1(
                "Nach dem 'compare' fehlt der 1. Check"),
        EXPRESSION_COMPARE_WITHOUT_CHECK2(
                "Nach dem 'compare' fehlt der 2. Check"),
        EXPRESSION_COMPARE_WITHOUT_CHECK3(
                "Nach dem 'compare' fehlt der 3. Check"),
        EXPRESSION_COMPARE_DOUBLE_EQ(
                "Doppelter [==] - Zweig im 'compare'"),
        EXPRESSION_COMPARE_DOUBLE_LESS(
                "Doppelter [<] - Zweig im 'compare'"),
        EXPRESSION_COMPARE_DOUBLE_GREATER(
                "Doppelter [>] - Zweig im 'compare'"),
        EXPRESSION_COMPARE_WRONG_ARGS(
                "In der Bedingung für 'compare' müssen zwei Argumente aufgeführt werden"),
        EXPRESSION_COMPARE_INVALID(
                "In dem compare fehlen die Bedingung/die Zweige/die Körper"),
        EXPRESSION_SWITCH_WITHOUT_BLOCK(
                "'switch' - Bedingung ohne Block"),
        EXPRESSION_SWITCH_WITHOUT_COMPARISION(
                "Nach dem 'switch' fehlt der Klammerausdruck"),
        EXPRESSION_SWITCH_INVALID(
                "In dem 'switch' fehlen der Bedingungsblock/Bedingungen/Einzelzweige"),
        EXPRESSION_WRONG_STEPLIST(
                "Unzulässige Aufzählung in Block/Klammer. Evtl. Trennzeichen vergessen."),
        EXPRESSION_INVALID_BRACKET_ON_LEFT(
                "Klammerausdruck auf der linken Seite nicht zugelassen."),
        EXPRESSION_UNKNOWN_LAST_TYPE(
                "Bisheriges Auswerteergebnis zweifelhaft"),
        EXPRESSION_INVALID_BLOCK_DEFINITION(
                "Ein Block darf nicht an eine Operation angeschlossen werden."),
        EXPRESSION_INVALID_STEPLIST_IN_BRACKET(
                "Die Argumente in einer Liste sind nicht korrekt getrennt"),
        EXPRESSION_VARIABLE_NO_IDENTIFIER(
                "Variablen müssen als Name ausgelegt sein."),

        UNABLE2CREATE_EXPRESSION_TREE_TOO_MANY_ELEMENTS(
                "Der Ausdruckbaum kann nicht korrekt aufgelöst werden, da zu viele Elemente"),
        UNABLE2CREATE_WITHOUT_EXPRESSION(
                "Es wurde kein Ausdrucksbaum erstellt"),

        END(
                "");

        private String myErrorMessage;

        public final String getErrorMessage()
        {
            return this.myErrorMessage;
        }

        ExceptionType(final String errorMessage)
        {
            this.myErrorMessage = errorMessage;
        }
    }

    private ExceptionType myType = null;

    public final ExceptionType getType()
    {
        return this.myType;
    }

    private ParserException     myBefore     = null;

    private final static String EMPTY_STRING = new String();

    private final static String THE_LINE     = new String("Zeile: ");

    private final static String THE_COLUMN   = new String(", Spalte: ");

    private final static String THE_ERROR    = new String("\n    Fehler: ");

    private ParserException(final String errorMessage)
    {
        super(errorMessage);
    }

    private ParserException(final String errorMessage, final ParserException before)
    {
        this(errorMessage);

        this.myBefore = before;
    }

    public ParserException(
            final ExceptionType type,
            final String errorMessage,
            final ParserException before)
    {
        this(THE_LINE
                .concat(String.valueOf(SourceStream.getInstance().currentLine()))
                .concat(THE_COLUMN)
                .concat(String.valueOf(SourceStream.getInstance().currentColumn()))
                .concat(THE_ERROR)
                .concat(type.getErrorMessage())
                .concat(errorMessage), before);
        this.myType = type;
    }

    public ParserException(final ExceptionType type, final String errorMessage)
    {
        this(type, errorMessage, null);
    }

    public ParserException(final ExceptionType type, final ParserException before)
    {
        this(type, EMPTY_STRING, before);
    }

    public ParserException(final ExceptionType type)
    {
        this(type, null, null);
    }

    @SuppressWarnings("unused")
    private ParserException(final Throwable arg0)
    {
        super(arg0);
    }

    @SuppressWarnings("unused")
    private ParserException(final String arg0, final Throwable arg1)
    {
        super(arg0, arg1);
    }

    public void print()
    {
        System.out.println(this.getMessage());

        if(this.myBefore != null)
            this.myBefore.print();
    }

}
