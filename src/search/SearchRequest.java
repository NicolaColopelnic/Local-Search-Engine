package search;

import java.util.ArrayList;
import java.util.List;

public class SearchRequest {
    private final List<String> pathFilters = new ArrayList<>();
    private final List<String> contentFilters = new ArrayList<>();

    private String sortStrategy = "default";

    public void addPathFilter(String path) { pathFilters.add(path); }
    public void addContentFilter(String content) { contentFilters.add(content); }

    public void setSortStrategy(String sortStrategy) { this.sortStrategy = sortStrategy; }

    public List<String> getPathFilters() { return pathFilters; }
    public List<String> getContentFilters() { return contentFilters; }
    public String getSortStrategy() { return sortStrategy; }

}