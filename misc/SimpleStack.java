package misc;

public class SimpleStack<T>
{
    private class StackItem<T>
    {
        private T            myData = null;

        private StackItem<T> myNext = null;

        final boolean hasNext()
        {
            return this.next() != null;
        }

        final StackItem<T> next()
        {
            return this.myNext;
        }

        final T data()
        {
            return this.myData;
        }

        StackItem(final T data, final StackItem<T> next)
        {
            this.myNext = next;
            this.myData = data;
        }
    }

    private StackItem<T> myHook = null;

    private int          mySize = 0;

    public final int size()
    {
        return mySize;
    }

    public final boolean isEmpty()
    {
        return this.myHook == null;
    }

    public final T push(final T item)
    {
        this.myHook = new StackItem<T>(item, this.myHook);
        ++this.mySize;
        return item;
    }

    public final T pop()
    {
        T result = this.top();

        if(!this.isEmpty())
        {
            this.myHook = this.myHook.next();
            --this.mySize;
        }
        return result;
    }

    public final T top()
    {
        return this.isEmpty() ? null : this.myHook.data();
    }

    private final T getAt(final int index, final StackItem<T> current)
    {
        return (current == null) ? null : ((index == 0) ? current.data() : this.getAt(index - 1, current.next()));
    }

    public final T getAt(final int index)
    {
        return this.getAt(index, this.myHook);
    }

    public SimpleStack()
    {
        // TODO Auto-generated constructor stub
    }

}
