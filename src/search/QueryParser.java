package search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// takes the user input and turns it into the search request object

public class QueryParser {

    public SearchRequest parse(String rawQuery) {
        SearchRequest request = new SearchRequest();
        // regex finds key:value or just the value
        Matcher matcher = Pattern.compile("((?:path|content|color):(?:\\([^\\)]+\\)|\\S+))|(\\([^\\)]+\\)|\\S+)").matcher(rawQuery);

        while (matcher.find()) {
            String part = matcher.group(0);
            if(part.contains(":")) {
                String[] parts = part.split(":", 2);
                String key = parts[0].toLowerCase();
                String value = parts[1];
                if (key.equalsIgnoreCase("path")) request.addPathFilter(value);
                else if(key.equalsIgnoreCase("content")) request.addContentFilter(value);
                else if (key.equalsIgnoreCase("color")) request.addColorFilter(value);
            } else {
                request.addContentFilter(part);
            }
        }
        return request;
    }
}