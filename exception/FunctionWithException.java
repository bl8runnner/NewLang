package exception;

@FunctionalInterface
public interface FunctionWithException<X, R>
{
    public R apply(final X x) throws Exception;

}
