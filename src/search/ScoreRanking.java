package search;

import java.util.List;

public class ScoreRanking implements Ranking {
    @Override
    public void sort(List<SearchResult> results) {
        results.sort((a,b) -> Double.compare(b.score(), a.score()));
    }
}
