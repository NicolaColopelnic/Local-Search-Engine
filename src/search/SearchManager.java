package search;

import java.util.List;

public class SearchManager {
    private final CleanQuery sanitizer = new CleanQuery();
    private final SearchRepository repository = new SearchRepository();

    public void search(String userQuery) {
        String safeQuery = sanitizer.sanitize(userQuery);

        List<SearchResult> results = repository.executeSearch(safeQuery);

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