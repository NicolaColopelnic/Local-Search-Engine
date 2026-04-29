package search;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchManager {
    private final QueryParser parser = new QueryParser();
    private final SearchRepository repository = new SearchRepository();

    private Ranking currentStrategy = new ScoreRanking();

    private final List<SearchObserver> observers = new ArrayList<>();

    public void addObserver(SearchObserver observer) {
        observers.add(observer);
    }

    public void setRankingStrategy(Ranking strategy) {
        currentStrategy = strategy;
    }

    public List<SearchResult> search(String userQuery) throws SQLException {
        for (SearchObserver observer : observers) {
            observer.onSearchPerformed(userQuery);
        }

        SearchRequest request = parser.parse(userQuery);
        List<SearchResult> results = repository.executeSearch(request);
        currentStrategy.sort(results);
        return results;
    }
}
