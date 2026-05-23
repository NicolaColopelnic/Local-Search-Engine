package search.ranking;

import search.SearchResult;

import java.util.List;

public interface Ranking {
    void sort(List<SearchResult> results);
}
