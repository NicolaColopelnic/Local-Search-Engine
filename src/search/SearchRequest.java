package search;

import java.util.ArrayList;
import java.util.List;

public class SearchRequest {
    private final List<String> pathFilters = new ArrayList<>();
    private final List<String> contentFilters = new ArrayList<>();

    public void addPathFilter(String path) { pathFilters.add(path); }
    public void addContentFilter(String content) { contentFilters.add(content); }

    public List<String> getPathFilters() { return pathFilters; }
    public List<String> getContentFilters() { return contentFilters; }
}