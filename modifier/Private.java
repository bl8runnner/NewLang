/**
 * 
 */
package modifier;


/**
 * @author ralf
 *
 */
public class Private extends Modifier
{
    private static Private INSTANCE = null;

    public static Private getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new Private();

        return INSTANCE;
    }

    private Private()
    {
        super("private");
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
