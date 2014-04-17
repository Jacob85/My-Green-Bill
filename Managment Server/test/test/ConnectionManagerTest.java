package test;

import com.mygreenbill.Exceptions.UserIdentityException;
import com.mygreenbill.common.ConnectionManager;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.registration.SimpleIdentityValidationResponse;
import junit.framework.TestCase;
import org.json.JSONObject;

/**
 * Created by Jacob on 3/22/14.
 */
public class ConnectionManagerTest extends TestCase
{
    public void testGetInstance() throws Exception
    {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        assertNotNull(connectionManager);

        connectionManager.sendToTrafficBlade(new JSONObject("{message: hello World}"));
        connectionManager.sendToTrafficBlade(new JSONObject("{message: hello World1111111}"));
        connectionManager.sendToTrafficBlade(new JSONObject("{message: hello World1111111wwwwwwwww}"));

       // connectionManager. processAckJson(new JSONObject("{messageID: 2, MessageType: ACK}"));

    }

    public void testSendToTrafficBlade() throws Exception
    {

    }

    public void testProcessAck() throws Exception
    {


    }

    public void testGetUserIdentity() throws Exception
    {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        SimpleIdentityValidationResponse response = null;

        response = connectionManager.getUserIdentity(null);
        assertNull(response);

        response = connectionManager.getUserIdentity("3eee3ee");
        assertNull(response);

        response = connectionManager.getUserIdentity("1234567");
        assertNull(response);

        response = connectionManager.getUserIdentity("39054664");
        assertNotNull(response);

        try
        {
            response = connectionManager.getUserIdentity("111111111");
        }catch (UserIdentityException e)
        {
            assertEquals(e.getMessage(), "User id: 111111111 does not exists!");
        }


    }


}
