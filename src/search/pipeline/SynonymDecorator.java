package search.pipeline;

import java.util.HashMap;
import java.util.Map;

public class SynonymDecorator extends QueryDecorator {

    private static final Map<String, String> synonymMap = new HashMap<>();
    static {
        synonymMap.put("img", "(img OR image OR photo OR jpg OR png OR jfif OR jpeg)");
        synonymMap.put("doc", "(doc OR documentation OR readme OR md OR txt)");
        synonymMap.put("code", "(code OR java OR src OR main)");
    }

    public SynonymDecorator(QueryBuilder builder) {
        super(builder);
    }

    @Override
    public String build(String input) {
        String data = super.build(input);

        for (Map.Entry<String, String> entry : synonymMap.entrySet()) {
            String keyword = entry.getKey();
            String expansion = entry.getValue();

            // (?i) makes it case insensitive
            // \\b makes sure to only match the whole word
            String regex = "(?i)\\b" + keyword + "\\b";
            data = data.replaceAll(regex, expansion);
        }

        return data;
    }
}
