package database;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:search_engine.db";

    public void initialize() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // metadata table:
            stmt.execute("CREATE TABLE IF NOT EXISTS files (path TEXT UNIQUE, filename TEXT);");

            // search engine table
            stmt.execute("CREATE VIRTUAL TABLE IF NOT EXISTS file_index USING fts5(filename, content);");

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void save(String path, String name, String content) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            String sql1 = "INSERT OR REPLACE INTO files VALUES (?, ?)";
            PreparedStatement p1 = conn.prepareStatement(sql1);
            //  PreparedStatement - acts as a template that prevents sql injection
            p1.setString(1, path);
            p1.setString(2, name);
            p1.executeUpdate();

            String sql2 = "INSERT INTO file_index VALUES (?, ?)";
            PreparedStatement p2 = conn.prepareStatement(sql2);
            p2.setString(1, name);
            p2.setString(2, content);
            p2.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}