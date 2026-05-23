package scanner;

import java.io.File;

public interface IndexingStrategy {
    String extractData(File file);
}
