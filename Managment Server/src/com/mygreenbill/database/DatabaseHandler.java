package com.mygreenbill.database;

import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.common.ConnectionManager;
import com.mygreenbill.common.GeneralUtilities;
import com.mygreenbill.common.GreenBillUser;
import com.mygreenbill.common.Status;
import com.mygreenbill.registration.FullRegistrationRequest;
import com.mygreenbill.registration.RegistrationRequestAbstract;
import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Jacob on 3/29/14.
 */
public class DatabaseHandler
{
    private static DatabaseHandler instance;
    private final Logger LOGGER = Logger.getLogger(DatabaseHandler.class);
    private final String isUserExistsString = "select isUserIdExist(?);";
    private final String isUserExistsAndActiveString = "select isUserExistsAndActive(?);";

    // ENUM which represent all the possible messages status
    public enum MessageStatus {sent, pending, failed}

    public static DatabaseHandler getInstance()
    {
        if (instance == null)
            instance = new DatabaseHandler();
        return instance;
    }

    private DatabaseHandler()
    {
        LOGGER.info("Database Handler was created");
    }

    public Status runInsertQuery(String query) throws DatabaseException
    {
        Status returnStatus = null;
        if (query == null)
        {
            returnStatus = new Status(Status.OperationStatus.FAILED, "Query is null");
            LOGGER.info(returnStatus);
            return returnStatus;
        }

        ConnectionManager connectionManager = null;
        Connection connection = null;
        Statement statement;
        try
        {
            //execute the query
            connectionManager = ConnectionManager.getInstance();
            connection = connectionManager.getDatabaseConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
            returnStatus = new Status(Status.OperationStatus.SUCCESS, "query: " + query + " was execute!");
            LOGGER.info(returnStatus);

            // close resources
            statement.close();

            return returnStatus;

        } catch (DatabaseException e)
        {
            returnStatus = new Status(Status.OperationStatus.FAILED, "Unable to open Database connection");
            LOGGER.error(returnStatus, e);
        } catch (InitException e)
        {
            returnStatus = new Status(Status.OperationStatus.FAILED, "Unable to init the Connection Manager Object");
            LOGGER.error(returnStatus, e);
        } catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage(),e.getCause());
        }
        finally
        {
            if (connectionManager != null)
                connectionManager.closeDatabaseConnection(connection);
        }
        return returnStatus;

    }

    public Status addSignInRecord(GreenBillUser user)
    {
        if (!user.isUserObjectFull(false))
        {
             LOGGER.info("Cannot add add new sign in event to user because user in not full.. exiting!");
            return new Status(Status.OperationStatus.FAILED, "User is not full");
        }
        try
        {
            return runInsertQuery("call AddUserLoginEvent (" + user.getUserId() + ", '" + user.getEmail() + "');");
        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List runGetQuery(String query) throws DatabaseException
    {
        if (query == null)
        {
            LOGGER.info("Unable to run get query: Query is null");
            return null;
        }
        ResultSet resultSet = null;
        ConnectionManager connectionManager = null;
        Connection connection = null;
        Statement statement = null;

        try
        {
            connectionManager = ConnectionManager.getInstance();
            connection = connectionManager.getDatabaseConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            LOGGER.info("Get query: " + query + " was execute!");

           // extract the result from the result set into list
            List toReturn = toList(resultSet);
            // close resources
            statement.close();
            return toReturn;
        } catch (DatabaseException e)
        {
            LOGGER.error("Unable to open Database connection", e);
        } catch (InitException e)
        {
            LOGGER.error("Unable to init the Connection Manager Object", e);
        } catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage(),e.getCause());
        }
        finally
        {
            if (connectionManager != null)
                connectionManager.closeDatabaseConnection(connection);
        }

        return null;
    }

    public Status runUpdateQuery(String query) throws DatabaseException
    {
        return runInsertQuery(query);
    }

    /**
     * Change the user password in the database with the new password,
     * The new password in already encrypted
     * @param user The user to change the password to
     * @param newPassword The new encrypted password
     * @return  {@link com.mygreenbill.common.Status} that represent the operation status
     */
    public Status changeUserPassword(GreenBillUser user, String newPassword)
    {
        if (!GeneralUtilities.isIdValid(user.getUserId()))
        {
           LOGGER.info("Cannot update user password, id is not valid!");
            return new Status(Status.OperationStatus.FAILED, "Cannot update user password, id is not valid");
        }
        if (newPassword == null || newPassword.isEmpty())
        {
            LOGGER.info("Cannot update user password, password (" +newPassword +") is null or empty!");
            return new Status(Status.OperationStatus.FAILED, "Cannot update user password, password (" +newPassword +") is null or empty!");
        }
        try
        {
            return runUpdateQuery("update user set password= '" + newPassword +"' where id=" + user.getUserId() + ";");
        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * return check if the user exist in the Database
     * @param id the user id
     * @return  true if exists else false
     */
    public boolean isUserExist(String id, boolean checkActive)
    {
        if (!GeneralUtilities.isIdValid(id))
        {
            LOGGER.info("Unable to check if user exists: invalid id");
            return false;
        }
        String queryString;
        if (checkActive)
        {
            queryString = isUserExistsAndActiveString.replace("?", id);
        }
        else
        {
            queryString = isUserExistsString.replace("?", id);
        }
        try
        {
            List list = runGetQuery(queryString);
            Map map = (Map) list.get(0);
            // get the first value
            Integer firstValue = (Integer) map.values().toArray()[0];
            //return the first value
            return firstValue == 1 ? true : false;

        } catch (DatabaseException e)
        {
            LOGGER.error(e);
            return false;
        }

       // return true;

    }

    /**
     * Add new User record t the Db and update all the user tables:
     * <ul>
     *     <li>User Table</li>
     *     <li>Add new login record</li>
     * </ul>
     * @param registrationRequest  The registration request
     * @return  {@link com.mygreenbill.common.Status} that represent the operation status
     */
    public Status registerUser(RegistrationRequestAbstract registrationRequest)
    {
        //call add new user stored procedure
        //String addUserQuery = "call "
        if (registrationRequest instanceof FullRegistrationRequest)
        {
            FullRegistrationRequest fullRegistrationRequest = (FullRegistrationRequest) registrationRequest;
            return registerUserFullRequest(fullRegistrationRequest);
        }
        return null;
    }

    private Status registerUserFullRequest(FullRegistrationRequest fullRegistrationRequest)
    {
        if (fullRegistrationRequest == null)
        {
            LOGGER.info("Cannot register user, the registration request is null");
            return new Status(Status.OperationStatus.FAILED, "Cannot register user, the registration request is null");
        }
        if (!fullRegistrationRequest.isRequestValid())
        {
            LOGGER.info("Cannot register user, the registration request is not valid!");
            return new Status(Status.OperationStatus.FAILED, "Cannot register user, the registration request is not valid!");
        }
        String addUserQuery = "call AddUser (" + fullRegistrationRequest.getId() + ", '" + fullRegistrationRequest.getEmail() +"', '" +
                               fullRegistrationRequest.getValidationResponse().getFatherName() + "', '"+
                               fullRegistrationRequest.getValidationResponse().getLastName() + "', '" +
                               fullRegistrationRequest.getEncryptPassword(EncryptionType.MD5) + "', '"+
                               EncryptionUtil.encryptString(fullRegistrationRequest.getEmail(), EncryptionType.MD5) + "' )";
        LOGGER.debug("Add user query string was constructed: " + addUserQuery);
        try
        {
            Status status =  runInsertQuery(addUserQuery);
            if (status.getOperationStatus() == Status.OperationStatus.SUCCESS)
                addSignInRecord(new GreenBillUser(fullRegistrationRequest));
            return status;
        } catch (DatabaseException e)
        {
            LOGGER.error("Filed to Add new user to db: ",e);
            return new Status(Status.OperationStatus.FAILED, "Filed to Add new user to db: " +e.getMessage());
        }
    }

    /**
     * Helper method that converts a ResultSet into a list of maps, one per row
     * @param rs
     * @return list of maps, one per row, with column name as the key
     * @throws SQLException if the connection fails
     */
    private  final List toList(ResultSet rs) throws SQLException
    {
        List wantedColumnNames = getColumnNames(rs);

        return toList(rs, wantedColumnNames);
    }
    /**
     * Helper method that maps a ResultSet into a list of maps, one per row
     * @param rs ResultSet
     * @param wantedColumnNames of columns names to include in the result map
     * @return list of maps, one per column row, with column names as keys
     * @throws SQLException if the connection fails
     */
    public final List toList(ResultSet rs, List wantedColumnNames) throws SQLException
    {
        List rows = new ArrayList();

        int numWantedColumns = wantedColumnNames.size();
        while (rs.next())
        {
            Map row = new TreeMap();

            for (int i = 0; i < numWantedColumns; ++i)
            {
                String columnName = (String)wantedColumnNames.get(i);
                Object value = rs.getObject(columnName);
                row.put(columnName, value);
            }

            rows.add(row);
        }

        return rows;
    }

    /**
     * Return all column names as a list of strings
     * @param rs query result set
     * @return list of column name strings
     * @throws SQLException if the query fails
     */
    public final List getColumnNames(ResultSet rs) throws SQLException
    {
        List columnNames = new ArrayList();

        ResultSetMetaData meta = rs.getMetaData();

        int numColumns = meta.getColumnCount();
        for (int i = 1; i <= numColumns; ++i)
        {
            columnNames.add(meta.getColumnName(i));
        }

        return columnNames;
    }

}
