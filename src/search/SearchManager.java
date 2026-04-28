package search;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SearchManager {
    //private final CleanQuery sanitizer = new CleanQuery();
    private final QueryParser parser = new QueryParser();
    private final SearchRepository repository = new SearchRepository();

    private Ranking currentStrategy = new ScoreRanking();
    private final SimpleDateFormat dateBuilder = new SimpleDateFormat("MMM dd, yyyy HH:mm");


    public void setRankingStrategy(Ranking startegy) {
        currentStrategy = startegy;
    }

    public void search(String userQuery) throws SQLException {
        //String safeQuery = sanitizer.sanitize(userQuery);
        SearchRequest request = parser.parse(userQuery);

        List<SearchResult> results = repository.executeSearch(request);
        currentStrategy.sort(results);

        System.out.println("\nSearch Results for: " + userQuery + " (Sorted by " + currentStrategy.getClass().getSimpleName() + ")");
        System.out.println();

        if (results.isEmpty()) {
            System.out.println("No results found.");
        } else {
            for (SearchResult res : results) {
                System.out.println("File: " + res.fileName());
                System.out.println("Context: " + res.preview());

                if(currentStrategy instanceof ScoreRanking) {
                    System.out.println("File Score: " + res.score());
                } else if(currentStrategy instanceof LastModifiedRanking) {
                    String str = dateBuilder.format(new Date(res.lastModified()));
                    System.out.println("Last Modified: " + str);
                }else if(currentStrategy instanceof LastAccessedRanking) {
                    String str = dateBuilder.format(new Date(res.lastAccessed()));
                    System.out.println("Last Accessed: " + str);
                }
                System.out.println();
            }
        }
    }
}
