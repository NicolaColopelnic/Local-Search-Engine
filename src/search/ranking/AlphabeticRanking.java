package search.ranking;

import search.SearchResult;

import java.util.List;

public class AlphabeticRanking implements Ranking {
    @Override
    public void sort(List<SearchResult> results) {
        results.sort((a, b) -> a.fileName().compareToIgnoreCase(b.fileName()));
    }
}
