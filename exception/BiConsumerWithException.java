package exception;

@FunctionalInterface
public interface BiConsumerWithException<X, Y>
{
    public void apply(final X x, final Y y) throws Exception;

}
