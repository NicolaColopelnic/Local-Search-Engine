package scanner;

import database.FileDocument;
import database.FileRepository;
import java.io.File;

public class Indexer {
    private final FileRepository repository;

    public Indexer(FileRepository repository) {
        this.repository = repository;
    }

    public void processAndIndex(File file, String content) {
        FileDocument doc = new FileDocument(
                file.getAbsolutePath(),
                file.getName(),
                file.lastModified(),
                file.length(),
                content
        );
        repository.save(doc);
    }
}