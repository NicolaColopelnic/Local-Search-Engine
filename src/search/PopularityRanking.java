package search;

import search.SearchResult;
import java.util.List;

public class PopularityRanking implements Ranking {
    @Override
    public void sort(List<SearchResult> results) {
        results.sort((a, b) -> Integer.compare(b.popularityCount(), a.popularityCount()));
    }
}