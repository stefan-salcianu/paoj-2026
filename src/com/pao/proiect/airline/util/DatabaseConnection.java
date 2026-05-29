package com.pao.proiect.airline.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private final Connection connection;

    private DatabaseConnection() {
        try {
            Properties props = new Properties();


            InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties");
            if (is == null) {
                throw new RuntimeException("db.properties nu a fost gasit in classpath.");
            }
            props.load(is);

            String driver   = props.getProperty("db.driver");
            String url      = props.getProperty("db.url");
            String user     = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Eroare la initializarea conexiunii la baza de date.", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
