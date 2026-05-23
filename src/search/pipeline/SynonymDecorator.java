package search.pipeline;

public class SynonymDecorator extends QueryDecorator {
    public SynonymDecorator(QueryBuilder builder) {
        super(builder);
    }

    @Override
    public String build(String input) {
        String data = super.build(input);
        String lower = data.toLowerCase();

        if (lower.toLowerCase().contains("img")) {
            data = data.replace("img", "(img OR image OR photo OR jpg OR png OR jfif OR jpeg)");
        }

        if (lower.contains("doc")) {
            data = data.replace("doc", "(doc OR documentation OR readme OR md OR txt)");
        }

        if (lower.contains("code")) {
            data = data.replace("code", "(code OR java OR src OR main)");
        }
        return data;
    }
}
