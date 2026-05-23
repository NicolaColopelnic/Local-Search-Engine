package search.pipeline;

public class QueryDecorator implements QueryBuilder {
    protected QueryBuilder decoratedBuilder;

    public QueryDecorator(QueryBuilder builder) {
        this.decoratedBuilder = builder;
    }

    @Override
    public String build(String input) {
        return decoratedBuilder.build(input);
    }
}
