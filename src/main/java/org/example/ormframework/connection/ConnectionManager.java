package org.example.ormframework.connection;

import org.example.ormframework.configuration.DBConnectionConfig;
import org.example.configuration.ConfigService;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {
    private Connection connection;
    private ConfigService configService;
    private final DBConnectionConfig dbConnectionConfig;

    public ConnectionManager() throws Exception {
        configService = new ConfigService();
        dbConnectionConfig = configService.readConfig("src\\main\\resources\\DbConnect.json", DBConnectionConfig.class);
    }

    public Connection getConnection() throws Exception {
        if (connection == null) {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create the connection
            connection = DriverManager.getConnection(dbConnectionConfig.getURL(), dbConnectionConfig.getUsername(), dbConnectionConfig.getPassword());

        }
        return connection;
    }

    public void closeConnection() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
