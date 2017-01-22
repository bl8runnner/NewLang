/**
 * 
 */
package modifier;


/**
 * @author ralf
 *
 */
public class Final extends Modifier
{
    private static Final INSTANCE = null;

    public static Final getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new Final();

        return INSTANCE;
    }

    private Final()
    {
        super("final");
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
