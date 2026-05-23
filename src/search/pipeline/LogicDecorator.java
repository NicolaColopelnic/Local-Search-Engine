package search.pipeline;

public class LogicDecorator extends QueryDecorator {
    public LogicDecorator(QueryBuilder builder) { super(builder); }

    @Override
    public String build(String input) {
        String data = super.build(input);
        if (data.isEmpty()) return data;

        String[] parts = data.split(" ");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (!result.isEmpty()) result.append(" ");

            // don't add * to color filter bc color is in the metadata table and standard sql doesn't treat * as a wildcard
            if (part.startsWith("color:")) {
                result.append(part);
            }
            // if it is a synonym group => add * only to the words inside the brackets and skip OR
            else if (part.contains("(") || part.contains(")")) {
                result.append(part.replaceAll("\\b(?!OR\\b)([a-zA-Z0-9]+)\\b", "$1*"));
            }
            // if it is a plain word or path: or content: => add *
            else if (!part.equalsIgnoreCase("OR") && !part.contains("*") && !part.isEmpty()) {
                result.append(part).append("*");
            }
            else {
                result.append(part);
            }
        }
        return result.toString();
    }
}