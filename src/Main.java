import database.DatabaseManager;
import scanner.FileScanner;
import java.io.File;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseManager db = new DatabaseManager();
        db.initialize();

        FileScanner scanner = new FileScanner();
        scanner.walk(new File("C:\\SD-project"));

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:search_engine.db");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT filename FROM file_index WHERE content MATCH 'database'");
            while (rs.next()) {
                System.out.println("Found in: " + rs.getString("filename"));
            }
        }
    }
}