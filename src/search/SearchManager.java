package search;

import java.sql.SQLException;
import java.util.List;

public class SearchManager {
    //private final CleanQuery sanitizer = new CleanQuery();
    private final QueryParser parser = new QueryParser();
    private final SearchRepository repository = new SearchRepository();

    public void search(String userQuery) throws SQLException {
        //String safeQuery = sanitizer.sanitize(userQuery);
        SearchRequest request = parser.parse(userQuery);

        List<SearchResult> results = repository.executeSearch(request);

        System.out.println("\nSearch Results for: " + userQuery);
        System.out.println();

        if (results.isEmpty()) {
            System.out.println("No results found.");
        } else {
            for (SearchResult res : results) {
                System.out.println("File: " + res.fileName());
                System.out.println("Context: " + res.preview());
                System.out.println();
            }
        }
    }
}
