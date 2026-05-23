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

        StringBuilder sql = new StringBuilder(
                "SELECT file_index.path, file_index.filename, snippet(file_index, 2, '[', ']', '...', 10) as preview, " +
                        "f.rank_score, f.last_modified, f.last_accessed, f.popularity_count, f.dominant_color " +
                        "FROM file_index JOIN files f ON file_index.path = f.path " +
                        "WHERE 1=1");

        if (!query.isEmpty()) {
            sql.append(" AND file_index MATCH ?");
        }

        if (!request.getColorFilters().isEmpty()) {
            sql.append(" AND f.dominant_color = ?");
        }

        sql.append(" ORDER BY f.rank_score DESC;");

        try(Connection conn = DbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if(!(query.isEmpty())) {
                pstmt.setString(paramIndex++, query);
            }

            if(!request.getColorFilters().isEmpty()) {
                pstmt.setString(paramIndex, request.getColorFilters().get(0));
            }

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                results.add(new SearchResult(
                        rs.getString("path"),
                        rs.getString("filename"),
                        rs.getString("preview"),
                        rs.getDouble("rank_score"),
                        rs.getLong("last_modified"),
                        rs.getLong("last_accessed"),
                        rs.getInt("popularity_count"),
                        rs.getString("dominant_color")));
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
            queryBuilder.append("path:").append(p);
            if (!p.endsWith("*")) queryBuilder.append("*");
        }

        // combine content filters with AND
        for (String c : request.getContentFilters()) {
            if (queryBuilder.length() > 0) queryBuilder.append(" AND ");
            queryBuilder.append(c);
        }

        return queryBuilder.toString();
    }
}
