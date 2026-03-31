package database;
import java.sql.*;

public class FileRepository {
    public void save(FileDocument doc) {
        // puts info into the files table, if the path already exists, it overwrites the old row with the new one
        String sqlMeta = "INSERT OR REPLACE INTO files VALUES(?,?,?,?)";
        // add the text content and filename into the search index
        String sqlIndex = "INSERT INTO file_index(filename, content) VALUES(?,?)";

        try (Connection conn = DbConnection.getConnection()) {
            //  PreparedStatement - acts as a template that prevents sql injection
            PreparedStatement p1 = conn.prepareStatement(sqlMeta);
            p1.setString(1, doc.path());
            p1.setLong(2, doc.lastModified());
            p1.setLong(3, doc.size());
            p1.setString(4, doc.fileName());
            p1.executeUpdate();

            PreparedStatement p2 = conn.prepareStatement(sqlIndex);
            p2.setString(1, doc.fileName());
            p2.setString(2, doc.content());
            p2.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}