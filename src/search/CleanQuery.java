package search;

public class CleanQuery {
    public String clean(String query) {
        // remove special characters that crash fts5
        String clean = query.replaceAll("[^a-zA-Z0-9 ]", " ");

        // wrap in quotes for phrase searching
        return "\"" + clean.trim() + "\"";
    }
}