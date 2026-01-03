package pt.ul.fc.css.weatherwise.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;


public enum DataSource {
    INSTANCE;

    private Connection connection;

    private static final String DB_URL = System.getProperty("db.url", "jdbc:postgresql://pgserver_ww:5432/testdb");
    private static final String DB_USER = System.getProperty("db.user", "postgres");
    private static final String DB_PASSWORD = System.getProperty("db.password", "admin");
    private static final int MAX_RETRIES = 5;
    private static final long RETRY_DELAY_MS = 3000;
    

    public DataSource connect() throws PersistenceException {
        if (connection != null) return INSTANCE;
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                System.out.println("Attempting to connect to database... (Attempt " + (attempt + 1) + ")");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connection successful.");
                return INSTANCE;
            } catch (SQLException e) {
                attempt++;
                if (attempt < MAX_RETRIES) {
                    System.out.println("Connection failed. Retrying in " + RETRY_DELAY_MS / 1000 + " seconds...");
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.err.println("Could not connect to the database after " + MAX_RETRIES + " attempts.");
                    throw new PersistenceException("Cannot connect to database", e);
                }
            }
        }
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException { 
        // since there is an error when calling getConnection multiple times, this solves by 'creating' a specific connection for that query.
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); 
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) { }
        }
    }

    public PreparedStatement prepare(String sql) throws PersistenceException {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new PersistenceException("Error preparing comment", e);
        }
    }

    public PreparedStatement prepareGetGenKey(String sql) throws SQLException {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public void beginTransaction() throws PersistenceException {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new PersistenceException("Error starting DB transaction", e);
        }
    }

    public void commit() throws PersistenceException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new PersistenceException("Error on commit", e);
        }
        startAutoCommit();
    }

    public void rollback() throws PersistenceException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new PersistenceException("Error on rollback!", e);
        }
        startAutoCommit();
    }

    private void startAutoCommit() throws PersistenceException {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new PersistenceException("Error starting auto commit", e);
        }
    }
    
    /**
     * Executes a simple SQL update statement (INSERT, UPDATE, DELETE, or DDL).
     * This method is primarily used for testing environment setup/cleanup.
     * It obtains a fresh connection and closes it immediately.
     * * @param sql The SQL statement to execute.
     * @throws PersistenceException If a database error occurs.
     */
    public void executeSQLUpdate(String sql) throws PersistenceException {
        try (Connection conn = getConnection(); // Gets a fresh connection
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            
        } catch (SQLException e) {
            // Throw a PersistenceException, wrapping the SQLException
            throw new PersistenceException("Error executing non-query SQL update: " + sql, e);
        }
    }
    
    
    
}
