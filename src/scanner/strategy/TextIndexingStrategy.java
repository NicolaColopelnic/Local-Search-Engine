package scanner.strategy;

import scanner.ContentReader;

import java.io.File;

public class TextIndexingStrategy implements IndexingStrategy {
    private final ContentReader reader = new ContentReader();

    @Override
    public String extractData(File file) {
        return reader.readAll(file);
    }
}
