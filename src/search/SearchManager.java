package search;
import database.DbConnection;
import java.sql.*;

public class SearchManager {
    public void search(String query) {
        String sql = "SELECT filename FROM file_index WHERE content MATCH ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("- " + rs.getString("filename"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}