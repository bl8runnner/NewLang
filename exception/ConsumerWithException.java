package exception;

@FunctionalInterface
public interface ConsumerWithException<X>
{
    public void apply(final X x) throws Exception;

}
