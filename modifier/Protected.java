/**
 * 
 */
package modifier;


/**
 * @author ralf
 *
 */
public class Protected extends Modifier
{
    private static Protected INSTANCE = null;

    public static Protected getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new Protected();

        return INSTANCE;
    }

    private Protected()
    {
        super("protected");
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
