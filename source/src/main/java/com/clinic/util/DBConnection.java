package com.clinic.util;

import com.clinic.exception.DatabaseException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread-safe Singleton Database Connection Manager.
 * Loads connection configuration from db.properties.
 * Implements fault tolerance, connection validation, and graceful degradation.
 */
public class DBConnection {
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static volatile DBConnection instance;

    private String driver;
    private String url;
    private String user;
    private String password;

    private DBConnection() {
        loadProperties();
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    private void loadProperties() {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
                this.driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
                this.url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/clinic_db?useSSL=false&serverTimezone=UTC");
                this.user = props.getProperty("db.user", "root");
                this.password = props.getProperty("db.password", "root123");
            } else {
                LOGGER.warning("db.properties not found in classpath. Falling back to default MySQL settings.");
                this.driver = "com.mysql.cj.jdbc.Driver";
                this.url = "jdbc:mysql://localhost:3306/clinic_db?useSSL=false&serverTimezone=UTC";
                this.user = "root";
                this.password = "root123";
            }
            Class.forName(driver);
            LOGGER.info("JDBC Driver initialized successfully: " + driver);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to locate JDBC Driver class: " + driver, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing DBConnection properties", e);
        }
    }

    /**
     * Obtains a new active JDBC connection.
     * @return Connection
     * @throws DatabaseException on connection failure
     */
    public Connection getConnection() throws DatabaseException {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn == null || conn.isClosed()) {
                throw new DatabaseException("Failed to establish active connection to database.");
            }
            return conn;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection error to: " + url, e);
            throw new DatabaseException("Unable to connect to clinic database. Ensure MySQL service is running.", e);
        }
    }

    /**
     * Safely closes a Connection ignoring nulls.
     */
    public static void close(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error closing resource: " + e.getMessage());
            }
        }
    }
}
