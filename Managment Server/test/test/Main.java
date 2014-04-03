package test;

import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.common.ConnectionManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jacob on 4/3/14.
 */
public class Main
{
    public static void main(String[] args) throws InitException, JSONException
    {
        ConnectionManager connectionManager = ConnectionManager.getInstance();

        connectionManager.sendToTrafficBlade(new JSONObject("{message: hello World}"));
        connectionManager.sendToTrafficBlade(new JSONObject("{message: Second message }"));
        connectionManager.sendToTrafficBlade(new JSONObject("{message: Third message }"));

    }
}
