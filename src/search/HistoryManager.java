package search;

import database.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// saves what the user types to suggest later and updates the importance of the files in the database

public class HistoryManager  implements SearchObserver{
    @Override
    public void onSearchPerformed(String query) {
        if (query.trim().isEmpty()) return; // prevents saving empty strings

        boolean isColorSearch = query.contains("color:");
        // ignore the qualifiers to update the popularity of matching files based on searched word
        String cleanForMatch = query.replaceAll("(path|content|color):", "").trim();

        // save the string typed by the user to history
        String sqlHistory = "INSERT INTO search_history(query, search_date) VALUES(?, ?)";

        // add 1 to the popularity of the matching file
        String sqlPopularity;
        if(isColorSearch){
            sqlPopularity = "UPDATE files SET popularity_count = popularity_count + 1 WHERE dominant_color = ?";
        } else {
            sqlPopularity = """
            UPDATE files SET popularity_count = popularity_count + 1 
            WHERE path IN (SELECT path FROM file_index WHERE file_index MATCH ?)
            """;
        }

        try (Connection conn = database.DbConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlHistory)) {
                ps1.setString(1, query.trim());
                ps1.setLong(2, System.currentTimeMillis());
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlPopularity)) {
                ps2.setString(1, cleanForMatch);
                ps2.executeUpdate();
            }

            conn.commit(); // makes both changes at the same time (saving history and updating popularity)
        } catch (SQLException e) {
            System.err.println("Observer Error: " + e.getMessage());
        }
    }

    // method called as the user types
    public List<String> getSuggestions(String input) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT query FROM search_history WHERE query LIKE ? ORDER BY search_date DESC LIMIT 5";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, input + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("query"));
            }
        } catch (SQLException e) { }
        return list;
    }
}
