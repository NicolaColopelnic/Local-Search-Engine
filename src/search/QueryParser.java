package search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// takes the user input and turns it into the search request object

public class QueryParser {

    public SearchRequest parse(String rawQuery) {
        SearchRequest request = new SearchRequest();
        // regex finds key:value or just the value
        Matcher matcher = Pattern.compile("(path|content):(\\S+)|(\\S+)").matcher(rawQuery);

        while (matcher.find()) {
            if (matcher.group(1) != null) { // key:value found
                String key = matcher.group(1);
                String value = matcher.group(2);
                if (key.equalsIgnoreCase("path")) request.addPathFilter(value);
                else request.addContentFilter(value);
            } else if (matcher.group(3) != null) { // plain word found
                request.addContentFilter(matcher.group(3));
            }
        }
        return request;
    }
}