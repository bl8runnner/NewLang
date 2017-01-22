package expressions;

interface ExpressionType
{
    public enum Type
    {
        NONE, UNKNOWN, BOOL, LONG, DOUBLE, STRING, OBJECT,
    }

    public ExpressionType.Type expressionType();

    public void newType(ExpressionType.Type type);

}
