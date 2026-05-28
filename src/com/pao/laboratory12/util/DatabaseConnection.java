package com.pao.laboratory12.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("com/pao/laboratory12/resources/db.properties")) {
            if (is == null) {
                throw new IOException("Nu gasesc db.properties in resources/");
            }
            props.load(is);
        }
        String url  = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        // Incarcam explicit driver-ul — necesar fara ServiceLoader (proiecte non-Maven)
        String driverClass = url.contains("mysql")  ? "com.mysql.cj.jdbc.Driver"
                           : url.contains("sqlite") ? "org.sqlite.JDBC"
                           :                          "org.h2.Driver";
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC negasit: " + driverClass
                    + ". Adauga JAR-ul in File → Project Structure → Modules → Dependencies.", e);
        }

        this.connection = DriverManager.getConnection(url, user, pass);

        try (var stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException ignored) {
            // ignorat pe MySQL/H2
        }
    }

    public static synchronized DatabaseConnection getInstance()
            throws IOException, SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
