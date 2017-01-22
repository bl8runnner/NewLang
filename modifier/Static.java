/**
 * 
 */
package modifier;


/**
 * @author ralf
 *
 */
public class Static extends Modifier
{
    private static Static INSTANCE = null;

    public static Static getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new Static();

        return INSTANCE;
    }

    private Static()
    {
        super("static");
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
