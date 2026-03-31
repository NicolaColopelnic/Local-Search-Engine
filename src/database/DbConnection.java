package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// create the database connection

public class DbConnection {
    private static final String URL = "jdbc:sqlite:search_engine.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}