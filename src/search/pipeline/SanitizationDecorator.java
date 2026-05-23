package search.pipeline;

public class SanitizationDecorator extends QueryDecorator {
    public SanitizationDecorator(QueryBuilder builder) {
        super(builder);
    }

    @Override
    public String build(String input) {
        String data = super.build(input);
        return data.replaceAll("[^a-zA-Z0-9 :*]", " ").replaceAll("\\s+", " ").trim();
    }
}
