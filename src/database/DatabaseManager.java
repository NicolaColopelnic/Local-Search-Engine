package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// building the database tables

public class DatabaseManager {
    public static void initialize() {
        // metadata table:
        String sqlMetadata = "CREATE TABLE IF NOT EXISTS files (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "path TEXT UNIQUE, " + // no two rows can have the same path - incremental indexing
                "last_modified INTEGER, " +
                "last_accessed INTEGER, " +
                "size INTEGER, " +
                "checksum TEXT," +
                "rank_score REAL, " +
                "popularity_count INTEGER DEFAULT 0, " +
                "dominant_color TEXT );";

        String sqlHistory = "CREATE TABLE IF NOT EXISTS search_history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "query TEXT, " +
                "search_date INTEGER);";

        // search engine table
        String sqlFTS = "CREATE VIRTUAL TABLE IF NOT EXISTS file_index USING fts5(path, filename, content);";
        // fts5 - breaks every word in a file into a fast index

        // open the connection to the database
        try (Connection connection = DbConnection.getConnection();
             Statement stm = connection.createStatement()) {
            // delete tables and start fresh
            stm.execute("DROP TABLE IF EXISTS files;");
            //stm.execute("DROP TABLE IF EXISTS file_index;");
            //stm.execute("DROP TABLE IF EXISTS search_history;");
            // stm is used to write in the database
            stm.execute(sqlMetadata);
            stm.execute(sqlFTS);
            stm.execute(sqlHistory);
            // create the tables

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
