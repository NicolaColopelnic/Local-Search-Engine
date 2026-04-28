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


    public void setRankingStrategy(Ranking strategy) {
        currentStrategy = strategy;
    }

    public List<SearchResult> search(String userQuery) throws SQLException {
        //String safeQuery = sanitizer.sanitize(userQuery);
        SearchRequest request = parser.parse(userQuery);

        List<SearchResult> results = repository.executeSearch(request);
        currentStrategy.sort(results);

       return results;
    }
}
