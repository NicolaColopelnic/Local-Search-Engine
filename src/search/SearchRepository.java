package search;

import database.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchRepository {
    public List<SearchResult> executeSearch(String sanitizedQuery) {
        List<SearchResult> results = new ArrayList<>();

        String sql = "SELECT filename, snippet(file_index, 1, '[', ']', '...', 10) as preview " +
                "FROM file_index WHERE content MATCH ?;";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sanitizedQuery);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(new SearchResult(rs.getString("filename"), rs.getString("preview")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }
}