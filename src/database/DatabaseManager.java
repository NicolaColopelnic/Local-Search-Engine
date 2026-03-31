package database;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:search_engine.db";

    public void initialize() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // metadata table
            stmt.execute("CREATE TABLE IF NOT EXISTS files (path TEXT UNIQUE, filename TEXT);");

            // search engine table
            stmt.execute("CREATE VIRTUAL TABLE IF NOT EXISTS file_index USING fts5(filename, content);");

        } catch (SQLException e) { e.printStackTrace(); }
    }

}