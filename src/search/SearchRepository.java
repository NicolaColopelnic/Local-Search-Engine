package search;

import database.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// sql logic for search operations

public class SearchRepository {
    public List<SearchResult> executeSearch(SearchRequest request) throws SQLException {
        List<SearchResult> results = new ArrayList<>();
        String query = buildSql(request);

        String sql = "SELECT filename, snippet(file_index, 2, '[', ']', '...', 10) as preview " +
                "FROM file_index WHERE file_index MATCH ?;";

        try(Connection conn = DbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, query);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                results.add(new SearchResult(rs.getString("filename"), rs.getString("preview")));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return results;
    }

    // convert the object to fts5 syntax
    public String buildSql(SearchRequest request) {
        StringBuilder queryBuilder = new StringBuilder();

        // combine the path filters with AND
        for (String p : request.getPathFilters()) {
            if (queryBuilder.length() > 0) queryBuilder.append(" AND "); // if there is already something in the string add AND
            queryBuilder.append("path:").append(p).append("*"); // allow partial path matching ( * = fts5 prefix operator)
        }

        // combine content filters with AND
        for (String c : request.getContentFilters()) {
            if (queryBuilder.length() > 0) queryBuilder.append(" AND ");
            queryBuilder.append("content:").append(c);
        }
        return queryBuilder.toString();
    }
}
