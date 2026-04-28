package search;

import java.util.List;

public interface Ranking {
    void sort(List<SearchResult> results);
}
