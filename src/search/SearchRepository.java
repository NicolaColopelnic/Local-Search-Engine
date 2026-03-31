package search;

import database.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// sql logic for search operations

public class SearchRepository {
    public List<SearchResult> executeSearch(String sanitizedQuery) {
        List<SearchResult> results = new ArrayList<>();
        // snippet() to extract context
        String sql = "SELECT filename, snippet(file_index, 2, '[', ']', '...', 10) as preview " +
                "FROM file_index WHERE file_index MATCH ?;";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sanitizedQuery);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // create a SearchResult object for every hit
                results.add(new SearchResult(rs.getString("filename"),
                        rs.getString("preview")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Search Error: " + e.getMessage());
        }
        return results;
    }
}