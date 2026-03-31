package scanner;

import database.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

// recursive traversal of the file system

public class FileScanner {
    private final Indexer indexer;
    private final ScanFilter filter;
    private final ContentReader reader;
    private int foldersScanned = 0;
    private int loopsDetected = 0;

    // track paths already visited to avoid symlink loops
    private final Set<String> visitedCanonicalPaths = new HashSet<>();

    public FileScanner(Indexer indexer, Configuration config) {
        this.indexer = indexer;
        this.filter = new ScanFilter(config);
        this.reader = new ContentReader();
    }

    public void scanDirectory(String rootPath) throws IOException {
        File root = new File(rootPath);
        if (root.exists()) walk(root); // recursive traversal
    }

    // recursive walk through directory tree
    private void walk(File folder) throws IOException {
        String canonicalPath = folder.getCanonicalPath();

        if (visitedCanonicalPaths.contains(canonicalPath)) {
            loopsDetected++;
            System.out.println("[LOOP DETECTED] Skipping: " + folder.getPath());
            return;
        }
        visitedCanonicalPaths.add(canonicalPath);

        foldersScanned++;
        File[] list = folder.listFiles();
        if (list == null) return;

        for (File file : list) {
            if (filter.isIgnored(file)) continue;

            if (file.isDirectory()) {
                walk(file); // recursive call for subdirectories
            } else {
                // read content only from text files
                String content = "";
                if (filter.isTextFile(file.getName())) {
                    content = reader.readAll(file);
                }

                // hand off to indexer
                indexer.processAndIndexFile(file, content);
            }
        }
    }

    public int getFoldersScanned() { return foldersScanned; }
    public int getLoopsDetected() { return loopsDetected; }

}