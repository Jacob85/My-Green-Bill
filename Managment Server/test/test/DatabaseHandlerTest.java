package test;

import com.mygreenbill.database.DatabaseHandler;
import junit.framework.TestCase;

/**
 * Created by Jacob on 3/29/14.
 */
public class DatabaseHandlerTest extends TestCase
{
    public void testRunInsertQuery() throws Exception
    {

    }

    public void testRunGetQuery() throws Exception
    {

    }

    public void testIsUserExist() throws Exception
    {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        boolean result = false;

        result = databaseHandler.isUserExist(null, false);
        assertFalse(result);

        result = databaseHandler.isUserExist("dddd", false);
        assertFalse(result);

        result = databaseHandler.isUserExist("1234567", false);
        assertFalse(result);

        result = databaseHandler.isUserExist("038054664", false);
        assertTrue(result);

        result = databaseHandler.isUserExist("038054444", false);
        assertFalse(result);

        result = databaseHandler.isUserExist("038054664", true);
        assertTrue(result);
    }
}
