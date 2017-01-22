package node;

public class NodeDispatcherResult<T>
{
    private T myValue;
    
    public final T value()
    {
        return this.myValue;    
    }
    
    private boolean myIsInitial = true;
    
    public final boolean isInitial()
    {
        return this.myIsInitial;
    }
    
    public NodeDispatcherResult(final T value)
    {
        this.myValue = value;
        this.myIsInitial = false;
    }
    
    public NodeDispatcherResult()
    {
    }

}
