package com.mygreenbill.common;

import com.mygreenbill.Exceptions.ConfigurationException;
import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.Exceptions.InitException;
import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Jacob on 3/18/14.
 */
public class ConnectionManager
{
    private static ConnectionManager instance = null;
    private final Logger LOGGER = Logger.getLogger(ConnectionManager.class);

    private PoolProperties jdbcPoolProperties;
    private DataSource datasource;
    private int databasePort;
    private String databaseHost;
    private String databaseUser;
    private String databasePassword;
    private String databaseName;

    public static ConnectionManager getInstance() throws InitException
    {
        if (instance == null)
            instance = new ConnectionManager();
        return instance;
    }

    private ConnectionManager() throws InitException
    {
        init();
        initConnectionPool();
    }

    private void initConnectionPool()
    {
        jdbcPoolProperties = new PoolProperties();

        jdbcPoolProperties.setUrl("jdbc:mysql://" + databaseHost + ":"+ databasePort +"/" + databaseName+"?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8");
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
            this.databaseHost = configurationManager.getProperty("database_host");
            this.databaseUser = configurationManager.getProperty("database_user");
            this.databasePassword = configurationManager.getProperty("database_password");
            this.databaseName = configurationManager.getProperty("database_name");
            this.databasePort = Integer.parseInt(configurationManager.getProperty("database_port"));
        }
        catch (ConfigurationException e)
        {
            LOGGER.error("unable to init " + this.getClass().getSimpleName() + " error in configuration manager");
            throw new InitException("could not init the ConnectionManager Class", e);
        }
    }

    public Connection getDatabaseConnection() throws DatabaseException
    {
        Connection connection = null;
        try
        {
            connection = datasource.getConnection();
        }
        catch (SQLException e)
        {
            LOGGER.error("Unable to get new  connection from connection pool: " + e.getMessage(),e);
            throw new DatabaseException("Unable to get new connection from connection pool", e.getCause());
        }

        LOGGER.info("Connection with DB was made");
        return connection;
    }

    public void closeDatabaseConnection(Connection toClose)
    {
        if (toClose != null)
        {
            try
            {
                toClose.close();
            }
            catch (SQLException ignore)
            {

            }
        }
    }
}
