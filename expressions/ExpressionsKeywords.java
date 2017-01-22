package expressions;

public class ExpressionsKeywords
{
    public enum Keyword
    {
        IF(
                "if"),
        DISPATCH(
                "dispatch"),
        CONST(
                "const"),
        COMPARE(
                "compare"),
        BREAK(
                "break"),
        RETURN(
                "return");

        private String myKeyword = null;

        Keyword(final String keyword)
        {
            this.myKeyword = keyword;
        }

        boolean checkExpression(final String expression)
        {
            return this.myKeyword.equals(expression);
        }
    }

    public final void tryKeywords(final String expression)
    {
        for(Keyword keyword : Keyword.values())
        {
            if(keyword.checkExpression(expression))
            {
                ;
            }
        }
    }
}
