package parser;

public class KeywordCheck
{

    private static KeywordCheck instance = null;

    public static KeywordCheck getInstance()
    {
        if(instance == null)
            instance = new KeywordCheck();

        return instance;
    }

    private KeywordCheck()
    {
    }

    private enum Checker
    {
        NAMESPACE(
                ParserNamespace.getInstance()),
        CLASS(
                ParserClass.getInstance()),
        MODIFIER(
                ParserModifier.getInstance());

        private KeywordChecker myKeywordChecker = null;

        boolean isKeyword(final String identifier) throws Exception
        {
            return this.myKeywordChecker.isKeyword(identifier);
        }

        Checker(final KeywordChecker keywordChecker)
        {
            this.myKeywordChecker = keywordChecker;
        }
    }

    public final boolean check4Keyword(final String identifier) throws Exception
    {
        for(Checker checker : Checker.values())
            if(checker.isKeyword(identifier))
                return true;

        return false;
    }

}
