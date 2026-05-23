package scanner;

import database.FileDocument;
import database.FileRepository;
import scanner.strategy.ImageIndexingStrategy;
import scanner.strategy.IndexingStrategy;
import scanner.strategy.TextIndexingStrategy;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

// handles incremental indexing logic,
// manages error handling, and performance metrics for the final report

public class Indexer {
    private final FileRepository repository;
    // performance metrics for report
    private int filesIndexed = 0;
    private int imagesIndexed = 0;
    private int filesSkipped = 0;
    private int filesFailed = 0;
    private long totalBytesIndexed = 0;

    private final PathScore scorer = new PathScore();

    private ScanListener listener;
    public void setListener(ScanListener listener) {
        this.listener = listener;
    }

    public Indexer(FileRepository repository) {
        this.repository = repository;
    }

    // main logic for file indexing, handles change detection
    public void processAndIndexFile(File file) {
        try {
            String path = file.getAbsolutePath();

            String mimeType = Files.probeContentType(file.toPath());

            IndexingStrategy strategy;
            if (mimeType != null && mimeType.startsWith("image")) {
                strategy = new ImageIndexingStrategy();
            } else {
                strategy = new TextIndexingStrategy();
            }

            // generate a unique SHA-256 fingerprint for the current file content
            String newChecksum = CheckSum.calculateChecksum(file.toPath());
            // get existing fingerprint from the database to detect modifications
            String oldChecksum = repository.getChecksum(path);

            double score = scorer.calculateScore(file);

            // stop if the file is unchanged
            if (newChecksum.equals(oldChecksum)) {
                filesSkipped++;
                if (listener != null) listener.onLog("[SKIP] No changes: " + file.getName());
                return;
            }

            // identify is the file is new or just updated
            if (oldChecksum.equals("")) {
                if (listener != null) listener.onLog("[NEW] " + file.getName());
            } else {
                if (listener != null) listener.onLog("[UPDATE] " + file.getName());
            }

            String extractedData = strategy.extractData(file);
            String content = "", color = "";

            if(strategy instanceof ImageIndexingStrategy) {
                imagesIndexed++;
                color = extractedData;
            } else {
                content = extractedData;
            }

            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            long lastAccessed = attrs.lastAccessTime().toMillis();
            long lastModified = attrs.lastModifiedTime().toMillis();
            filesIndexed++;
            totalBytesIndexed += file.length();

            // instantiate a file object
            FileDocument doc = new FileDocument(path, file.getName(), lastModified, lastAccessed, file.length(), content, newChecksum, score, color);
            repository.save(doc);

        } catch (Exception e) {
            filesFailed++;
            System.err.println("Indexing failed for: " + file.getName());
        }
    }

    public int getFilesIndexed() { return filesIndexed; }
    public int getImagesIndexed() { return imagesIndexed; }
    public int getFilesSkipped() { return filesSkipped; }
    public int getFilesFailed() { return filesFailed; }
    public long getTotalBytesIndexed() { return totalBytesIndexed; }
    public ScanListener getListener() { return listener; }
}
