package database;

import java.sql.*;

// connects the FileDocument to the Database, handles the database syntax

public class FileRepository {

    public void save(database.FileDocument doc) {

        // puts info into the files table, if the path already exists, it overwrites the old row with the new one
        String sqlMetadata = "INSERT OR REPLACE INTO files(path, last_modified, size, checksum) VALUES(?,?,?,?)";

        // delete the old search data for the path before adding the new version (fts5 tables don't have unique constraints)
        String sqlDeleteIndex = "DELETE FROM file_index WHERE path = ?";

        // add the actual text content and filename into the search index
        String sqlIndex = "INSERT INTO file_index(path, filename, content) VALUES(?,?,?)";

        try (Connection connection = database.DbConnection.getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(sqlDeleteIndex)) {
                //  PreparedStatement - acts as a template that prevents sql injection
                pstmt.setString(1, doc.path()); // fill first ? with th epath
                pstmt.executeUpdate(); // save data to the disk
            }

            try (PreparedStatement pstmt = connection.prepareStatement(sqlMetadata)) {
                pstmt.setString(1, doc.path());
                pstmt.setLong(2, doc.lastModified());
                pstmt.setLong(3, doc.size());
                pstmt.setString(4, doc.checksum());
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = connection.prepareStatement(sqlIndex)) {
                pstmt.setString(1, doc.path());
                pstmt.setString(2, doc.fileName());
                pstmt.setString(3, doc.content());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Database update rrror: " + e.getMessage());
        }
    }

    //
    public String getChecksum(String path) {
        String sql = "SELECT checksum FROM files WHERE path = ?";
        // retrieve the hash value from the files table for a specific file path
        try (Connection conn = database.DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, path);
            ResultSet rs = pstmt.executeQuery();
            // execute the query and get a ResultSet
            return rs.next() ? rs.getString("checksum") : "";

        } catch (SQLException e) {
            return "";
        }
    }
}