/**
 * 
 */
package modifier;


/**
 * @author ralf
 *
 */
public class Abstract extends Modifier
{
    private static Abstract INSTANCE = null;

    public static Abstract getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new Abstract();

        return INSTANCE;
    }

    private Abstract()
    {
        super("abstract");
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
