package org.gamescore.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static HikariDataSource dataSource;

    static {

        try {
            // Load the properties file
            Properties properties = new Properties();
            properties.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("application.properties"));

            // Set up HikariCP configuration
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));
            config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("hikari.max-pool-size", "10")));

            // Initialize the data source
            dataSource = new HikariDataSource(config);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

