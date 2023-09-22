package org.example.ormframework.connection;

import org.example.ormframework.configuration.DBConnectionConfig;
import org.example.configuration.ConfigService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private Connection connection;
    private ConfigService configService;
    private final DBConnectionConfig dbConnectionConfig;

    public ConnectionManager() throws IOException {
        configService = new ConfigService();
        dbConnectionConfig = configService.readConfig("src\\main\\resources\\DbConnect.json", DBConnectionConfig.class);
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                // Load the JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Create the connection
                connection = DriverManager.getConnection(dbConnectionConfig.getURL(), dbConnectionConfig.getUsername(), dbConnectionConfig.getPassword());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to establish a database connection.");
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to close the database connection.");
            }
        }
    }
}
