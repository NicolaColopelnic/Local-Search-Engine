package search.pipeline;

public class SimpleQueryBuilder implements QueryBuilder {
    @Override
    public String build(String input) {
        return input;
    }
}
