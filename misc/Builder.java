package misc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Builder<T>
{
    private Class<T> myClass;

    public Builder(Class<T> theClass)
    {
        this.myClass = theClass;
    }

    public T create(final Object[] arguments)
    {
        @SuppressWarnings("unchecked")
        Constructor<T>[] constructors = (Constructor<T>[]) this.myClass.getDeclaredConstructors();

        for(Constructor<T> constructor : constructors)
        {
            Class<?>[] parameters = constructor.getParameterTypes();

            if(parameters.length == arguments.length)
            {
                try
                {
                    return (T) constructor.newInstance(arguments);
                }
                catch(IllegalArgumentException exception)
                {
                    // TODO Auto-generated catch block
                    exception.printStackTrace();
                }
                catch(InstantiationException exception)
                {
                    // TODO Auto-generated catch block
                    exception.printStackTrace();
                }
                catch(IllegalAccessException exception)
                {
                    // TODO Auto-generated catch block
                    exception.printStackTrace();
                }
                catch(InvocationTargetException exception)
                {
                    // TODO Auto-generated catch block
                    exception.printStackTrace();
                }
            }
        }

        return null;
    }
}
