/**
 * 
 */
package modifier;


/**
 * @author ralf
 *
 */
public class Public extends Modifier
{
    private static Public INSTANCE = null;

    public static Public getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new Public();

        return INSTANCE;
    }

    private Public()
    {
        super("public");
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
