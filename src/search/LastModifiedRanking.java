package search;

import java.util.List;

public class LastModifiedRanking implements Ranking {
    @Override
    public void sort(List<SearchResult> results) {
        results.sort((a, b) -> Long.compare(b.lastModified(), a.lastModified()));
    }
}
