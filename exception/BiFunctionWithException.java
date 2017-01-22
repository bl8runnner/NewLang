package exception;

@FunctionalInterface
public interface BiFunctionWithException<X, Y, R>
{
    public R apply(final X x, final Y y) throws Exception;

}
