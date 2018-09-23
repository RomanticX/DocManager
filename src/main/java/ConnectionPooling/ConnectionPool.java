package ConnectionPooling;

import javax.sql.DataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import java.sql.Connection;

public class ConnectionPool {
    // JDBC Driver Name & Database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String JDBC_DB_URL = "jdbc:mysql://localhost:3306/office_datas?" +
            "useUnicode=true&characterEncoding=utf-8";
    // JDBC Database Credentials
    static final String JDBC_USER = "root";
    static final String JDBC_PASS = "";
    private static GenericObjectPool gPool = null;
    public static Connection sCon = null;

    @SuppressWarnings("unused")
    public DataSource setUpPool() throws Exception {
        Class.forName(JDBC_DRIVER);
        // Creates an Instance of GenericObjectPool That Holds Our Pool of Connections Object!
        gPool = new GenericObjectPool();
        gPool.setMaxActive(2);
        // Creates a ConnectionFactory Object Which Will Be Use by the Pool to Create the Connection Object!
        ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASS);
        // Creates a PoolableConnectionFactory
        // That Will Wraps the Connection Object Created by the ConnectionFactory to Add Object Pooling Functionality!
        PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool,
                null, null, false, true);

        return new PoolingDataSource(gPool);
    }

    public GenericObjectPool getConnectionPool() {
        return gPool;
    }

    public void closePool() throws Exception {
        gPool.close();
    }

        // This Method Is Used To Print The Connection Pool Status
    public void printDbStatus() {
        System.out.println("Max.: " + getConnectionPool().getMaxActive() + "; Active: "
                + getConnectionPool().getNumActive() + "; Idle: " + getConnectionPool().getNumIdle());
    }

    public void createConnection() throws Exception {
        DataSource dataSource = this.setUpPool();
        this.printDbStatus();
        sCon = dataSource.getConnection();
        this.printDbStatus();
    }
}
