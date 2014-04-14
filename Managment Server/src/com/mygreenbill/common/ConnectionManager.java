package com.mygreenbill.common;

import com.mygreenbill.Exceptions.*;
import com.mygreenbill.registration.SimpleIdentityValidationResponse;
import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;
import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jacob on 3/18/14.
 */
public class ConnectionManager
{
    private static ConnectionManager instance = null;
    private final Logger LOGGER = Logger.getLogger(ConnectionManager.class);

    private AtomicInteger jsonRequestID = new AtomicInteger(0);
    private ConcurrentHashMap<String, RequestJson> sentJsonRequestsMap;
    private int resendingSleepTime;
    private int maxNumberOfResendAttempts;
    private DatagramSocket socket;
    private Thread sender;
    private final int MAX_DATAGRAM_SIZE = 65508;

    private PoolProperties jdbcPoolProperties;
    private DataSource datasource;

    private int innerConnectionPort;
    private int databasePort;
    private String trafficBladeIp;
    private String databaseHost;
    private String databaseUser;
    private String databasePassword;
    private String databaseName;
    private String identityUrl;

    public static ConnectionManager getInstance() throws InitException
    {
        if (instance == null)
            instance = new ConnectionManager();
        return instance;
    }

    private ConnectionManager() throws InitException
    {
        sentJsonRequestsMap = new ConcurrentHashMap<String, RequestJson>();

        init();
        initConnectionPool();

    }

    private void initConnectionPool()
    {
        //todo yaki - move the hard coded configuration to file
        jdbcPoolProperties = new PoolProperties();
        jdbcPoolProperties.setUrl("jdbc:mysql://" + databaseHost + ":3306/" + databaseName);
        jdbcPoolProperties.setDriverClassName("com.mysql.jdbc.Driver");
        jdbcPoolProperties.setUsername(databaseUser);
        jdbcPoolProperties.setPassword(databasePassword);
        jdbcPoolProperties.setJmxEnabled(true);
        jdbcPoolProperties.setTestWhileIdle(false);
        jdbcPoolProperties.setTestOnBorrow(true);
        jdbcPoolProperties.setValidationQuery("SELECT 1");
        jdbcPoolProperties.setTestOnReturn(false);
        jdbcPoolProperties.setValidationInterval(30000);
        jdbcPoolProperties.setTimeBetweenEvictionRunsMillis(30000);
        jdbcPoolProperties.setMaxActive(100);
        jdbcPoolProperties.setInitialSize(10);
        jdbcPoolProperties.setMaxWait(10000);
        jdbcPoolProperties.setRemoveAbandonedTimeout(60);
        jdbcPoolProperties.setMinEvictableIdleTimeMillis(30000);
        jdbcPoolProperties.setMinIdle(10);
        jdbcPoolProperties.setLogAbandoned(true);
        jdbcPoolProperties.setRemoveAbandoned(true);
        jdbcPoolProperties.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        datasource = new DataSource();
        datasource.setPoolProperties(jdbcPoolProperties);
    }

    private void init() throws InitException
    {
        try
        {
            ConfigurationManager configurationManager = ConfigurationManager.getInstance();
            // init the class members from the Configuration file
            this.innerConnectionPort = configurationManager.getValueWithDefault("blades_listen_port", 9980);
            this.trafficBladeIp = configurationManager.getProperty("traffic_blade_ip");
            this.databaseHost = configurationManager.getProperty("database_host");
            this.databaseUser = configurationManager.getValueWithDefault("database_user", "root");
            this.databasePassword = configurationManager.getProperty("database_password");
            this.databaseName = configurationManager.getValueWithDefault("database_name", "mygreenbilldb");
            this.databasePort = configurationManager.getValueWithDefault("database_port", 3306);
            this.identityUrl = configurationManager.getProperty("identity_verification_url");
            this.resendingSleepTime = configurationManager.getValueWithDefault("sleep_time_before_resending_json_request", 6000);
            this.maxNumberOfResendAttempts = configurationManager.getValueWithDefault("max_number_of_request_resend_attempts", 6);
            this.socket = new DatagramSocket();

            // start the sender thread
            sender = new Thread(new JsonRequestSender(resendingSleepTime));
            sender.start();
            LOGGER.debug("Sender Thread has Started");

            Thread listeningThread = new Thread(new InnerCommunicationListening());
            listeningThread.start();
            LOGGER.debug("Listening Thread has started");

        } catch (ConfigurationException e)
        {
            LOGGER.error("unable to init " + this.getClass().getSimpleName() + " error in configuration manager");
            throw new InitException("could not init the ConnectionManager Class", e);
        } catch (SocketException e)
        {
            LOGGER.error("unable to init " + this.getClass().getSimpleName() + " failed to init datagram", e);
            throw new InitException("could not init the ConnectionManager Class", e);
        }

        LOGGER.debug("init process finished successfully");

    }


    public void sendToTrafficBlade(JSONObject jsonMessage)
    {
        if (jsonMessage == null)
        {
            LOGGER.info("Received null message to send to client... do nothing");
        }


        // create new JsonRequest Object and add it to the sentMap -> then interrupt the thread
        RequestJson newRequestJson = new RequestJson(prepareJsonMessageToDeliver(jsonMessage), 0, jsonRequestID.getAndIncrement());
        sentJsonRequestsMap.put(String.valueOf(newRequestJson.getMessageId()), newRequestJson);

        sender.interrupt();
    }

    public Connection getDatabaseConnection() throws DatabaseException
    {
        Connection connection = null;
        try
        {
            connection = datasource.getConnection();
        } catch (SQLException e)
        {
            LOGGER.error("Unable to get new  connection from connection pool: " + e.getMessage(), e);
            throw new DatabaseException("Unable to get new connection from connection pool", e.getCause());
        }
        return connection;
    }

    public void closeDatabaseConnection(Connection toClose)
    {
        if (toClose != null)
        {
            try
            {
                toClose.close();
            } catch (SQLException ignore){}
        }
    }

    public SimpleIdentityValidationResponse getUserIdentity(String id) throws UserIdentityException
    {
        if (!GeneralUtilities.isIdValid(id))
        {
            LOGGER.info("Invalid id received: " + id);
            return null;
        }
        InputStream inputStream = null;
        try
        {
            URL url = new URL(identityUrl + "?id=" + id);
            inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = readAll(bufferedReader);
            if (jsonText.equalsIgnoreCase("false"))
            {
                //the response from the identity validation was false => the user does not exists
                throw new UserIdentityException("User id: " + id + " does not exists!");
            }
            JSONObject jsonObject = new JSONObject(jsonText);
            return new SimpleIdentityValidationResponse(jsonObject);

        } catch (MalformedURLException e)
        {
            LOGGER.error("Unable to create new URL Object from url: "+identityUrl + " message: " +e.getMessage(),e);
        } catch (IOException e)
        {
            LOGGER.error(e);
        } catch (JSONException e)
        {
            LOGGER.error("Unable to create new JSOn Object from response",e);
        } catch (CorruptIdentityParameter corruptIdentityParameter)
        {
            LOGGER.error(corruptIdentityParameter);
        }
        finally
        {
          if (inputStream != null)
          {
              try
              {
                  inputStream.close();
              } catch (IOException ignored){}
          }
        }

        return null;
    }

    // reads al text from buffer reader - return the string that was read
    private String readAll(Reader rd) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    public void processAckJson(JSONObject ackJson)
    {
        if (ackJson == null)
            return;
        try
        {
            String id = String.valueOf(ackJson.getInt("messageID"));
            String messageType = ackJson.getString("MessageType");
            if (messageType.equals("ACK"))
            {
                // remove the JSON message from the resend Map
                sentJsonRequestsMap.remove(id);
                for (String key : sentJsonRequestsMap.keySet())
                {
                    this.LOGGER.debug("Key is: " + key);
                }
                this.LOGGER.info("Removing JsonRequestID " + id+ " from the resending map");
                this.LOGGER.debug("Resend map size is: " + sentJsonRequestsMap.size());
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * adds a incremental message id in the originalJsonMessage
     * calculate the originalJsonMessage MD5 and wrap it with a new JSON Object that contain 2 key value pairs:
     *  <ul>
     *      <li>Message: originalJsonMessage (include the message id)</li>
     *      <li>CheckSum: the originalJsonMessage MD5</li>
     *  </ul>
     * @param originalJsonMessage
     * @return
     */
    private JSONObject prepareJsonMessageToDeliver(JSONObject originalJsonMessage)
    {
        if (originalJsonMessage == null)
            return null;
        try
        {
            originalJsonMessage.put("messageID", this.jsonRequestID.get());
            JSONObject toReturn = new JSONObject();
            toReturn.put("Message", originalJsonMessage);
            toReturn.put("CheckSum", EncryptionUtil.encryptString(originalJsonMessage.toString(), EncryptionType.MD5));
            LOGGER.info("Composed Json request message: " + toReturn.toString());
            return toReturn;
        } catch (JSONException e)
        {
            LOGGER.error("Failed to compose new json Object ", e);
        }
        return null;
    }




    public class JsonRequestSender implements GreenBillClient
    {
        private int threadSleepTime;
        private final Logger LOGGER = Logger.getLogger(JsonRequestSender.class);
        public JsonRequestSender(int threadSleepTime)
        {
            this.threadSleepTime = threadSleepTime;
        }

        @Override
        public void run()
        {

            while (true)
            {
                if  (sentJsonRequestsMap.size() == 0)
                {
                    try
                    {
                        this.LOGGER.debug("Nothing to Resend Sleeping for " + threadSleepTime + "...");
                        Thread.sleep(threadSleepTime);
                    } catch (InterruptedException e)
                    {
                        this.LOGGER.debug("JsonRequestSender sleep was interrupted: " + e.getMessage());
                    }
                    finally
                    {
                        continue;
                    }
                }
                this.LOGGER.debug(sentJsonRequestsMap.size() + " Requests to send, resending them..");
                Iterator iterator = sentJsonRequestsMap.entrySet().iterator();
                while (iterator.hasNext())
                {
                    Map.Entry pairs = (Map.Entry)iterator.next();
                    RequestJson toResend = (RequestJson) pairs.getValue();
                    if (toResend.getNumberOfResendingAttempts() < maxNumberOfResendAttempts)
                    {
                        sendMessage(toResend);
                        toResend.incrementResendAttempt();
                    }
                    else
                    {
                        this.LOGGER.info("No more attempts to resend the Json request! already failed: " + toResend.getNumberOfResendingAttempts()
                                        + " Disposing the request: " + toResend.getRequest().toString());
                        iterator.remove();
                    }
                }
                //after all is sent... sleep for the sleep time
                this.LOGGER.debug("All Requests sent.. sleeping for: " + threadSleepTime + " ms");
                try
                {
                    Thread.sleep(threadSleepTime);
                } catch (InterruptedException e)
                {
                    this.LOGGER.debug("JsonRequestSender sleep was interrupted: " + e.getMessage());
                }
            }


        }

        @Override
        public void sendMessage(RequestJson message)
        {
            try
            {

                InetAddress address = InetAddress.getByName(trafficBladeIp);
                byte[] buffer = message.toString().getBytes();
                if (buffer.length < MAX_DATAGRAM_SIZE)
                {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, innerConnectionPort);
                    socket.send(packet);
                }
                else
                {
                    LOGGER.info("Message size " + buffer.length +" in bigger than the max datagram size: " + MAX_DATAGRAM_SIZE);
                }

            } catch (UnknownHostException e)
            {
                LOGGER.error("Error with host name: " + trafficBladeIp, e);
                return;
            } catch (IOException e)
            {
                LOGGER.info("Unable to send the request: " + message, e);
                return;
            }
        }
    }

    public class InnerCommunicationListening implements GreenBillServer
    {

        private DatagramSocket datagramSocket;
        private final Logger LOGGER = Logger.getLogger(InnerCommunicationListening.class);
        public InnerCommunicationListening() throws InitException
        {
            try
            {
                datagramSocket = new DatagramSocket(innerConnectionPort);
            } catch (SocketException e)
            {
                this.LOGGER.error("Unable to start the InnerCommunicationListening thread ", e);
                throw new InitException("Unable to start the InnerCommunicationListening thread");
            }
        }

        @Override
        public void run()
        {
           listen();
        }

        @Override
        public void listen()
        {
            while (true)
            {
                byte[] buffer = new byte[MAX_DATAGRAM_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, MAX_DATAGRAM_SIZE);
                try
                {
                    // read the datagram;
                    datagramSocket.receive(packet);
                    // the data has read into buffer
                    buffer = packet.getData();
                    String str = new String(buffer, "UTF-8");
                    JSONObject ob = new JSONObject(str);
                    this.LOGGER.info("New message received, composed it into JSON: " + ob.toString());
                    processAckJson(ob);
                    // todo yaki - to remove from here
                    //just for debugging
                    //processAckJson(new JSONObject("{messageID: 1, MessageType: ACK}"));
                } catch (IOException e)
                {
                    this.LOGGER.error(e);
                } catch (JSONException e)
                {
                    this.LOGGER.error(e);
                }
            }

        }
    }
}
