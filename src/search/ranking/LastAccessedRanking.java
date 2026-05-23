package search.ranking;

import search.SearchResult;

import java.util.List;

public class LastAccessedRanking implements Ranking {
    @Override
    public void sort(List<SearchResult> results) {
        results.sort((a, b) -> Long.compare(b.lastAccessed(), a.lastAccessed()));
    }
}
