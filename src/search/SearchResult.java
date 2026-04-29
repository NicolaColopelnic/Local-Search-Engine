package search;

// object representing a database match, a search hit

public record SearchResult(String fileName, String preview, double score, long lastModified, long lastAccessed,int popularityCount) {
}
