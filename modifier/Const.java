/**
 * 
 */
package modifier;


/**
 * @author ralf
 *
 */
public class Const extends Modifier
{
    private static Const INSTANCE = null;

    public static Const getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new Const();

        return INSTANCE;
    }

    private Const()
    {
        super("const");
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
