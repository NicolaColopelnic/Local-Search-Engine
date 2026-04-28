package scanner;

import database.FileDocument;
import database.FileRepository;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

// handles incremental indexing logic,
// manages error handling, and performance metrics for the final report

public class Indexer {
    private final FileRepository repository;
    // performance metrics for report
    private int filesIndexed = 0;
    private int filesSkipped = 0;
    private int filesFailed = 0;
    private long totalBytesIndexed = 0;

    private final PathScore scorer = new PathScore();

    public Indexer(FileRepository repository) {
        this.repository = repository;
    }

    // main logic for file indexing, handles change detection
    public void processAndIndexFile(File file, String content) {
        try {
            String path = file.getAbsolutePath();
            // generate a unique SHA-256 fingerprint for the current file content
            String newChecksum = CheckSum.calculateChecksum(file.toPath());
            // get existing fingerprint from the database to detect modifications
            String oldChecksum = repository.getChecksum(path);

            double score = scorer.calculateScore(file);

            // stop if the file is unchanged
            if (newChecksum.equals(oldChecksum)) {
                filesSkipped++;
                System.out.println("[SKIP] No changes detected for: " + file.getName());
                return;
            }

            // identify is the file is new or just updated
            if (oldChecksum.equals("")) {
                System.out.printf("[NEW] %s (Score: %.1f)%n", file.getName(), score);
            } else {
                System.out.printf("[UPDATE] %s (Score: %.1f)%n", file.getName(), score);
            }

            filesIndexed++;
            totalBytesIndexed += file.length();

            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            long lastAccessed = attrs.lastAccessTime().toMillis();
            long lastModified = attrs.lastModifiedTime().toMillis();

            // instantiate a file object
            FileDocument doc = new FileDocument(path, file.getName(), lastModified, lastAccessed, file.length(), content, newChecksum, score);
            repository.save(doc);

        } catch (Exception e) {
            filesFailed++;
            System.err.println("Indexing failed for: " + file.getName());
        }
    }

    public int getFilesIndexed() { return filesIndexed; }
    public int getFilesSkipped() { return filesSkipped; }
    public int getFilesFailed() { return filesFailed; }
    public long getTotalBytesIndexed() { return totalBytesIndexed; }
}
