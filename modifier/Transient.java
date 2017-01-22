/**
 * 
 */
package modifier;


/**
 * @author ralf
 *
 */
public class Transient extends Modifier
{
    private static Transient INSTANCE = null;

    public static Transient getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new Transient();

        return INSTANCE;
    }

    private Transient()
    {
        super("transient");
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see modifier.Modifier#checkBy(modifier.CombinationChecker)
     */
    @Override
    public void check(final Check4 check4) throws Exception
    {
        check4.check(this);
    }
    
}
