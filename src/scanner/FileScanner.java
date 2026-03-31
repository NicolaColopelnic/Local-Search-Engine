package scanner;

import database.FileDocument;
import database.FileRepository;
import java.io.File;

public class FileScanner {
    private FileRepository repository = new FileRepository();
    private ScanFilter filter = new ScanFilter();

    // recursive walk through directory tree
    public void walk(File folder) {
        File[] list = folder.listFiles();
        if (list == null) return;

        for (File file : list) {
            if (file.isDirectory()) {
                walk(file);
            } else if (filter.isTextFile(file.getName())) {
                try {
                    // extract the content of the file
                    String content = java.nio.file.Files.readString(file.toPath());
                    // create aa document record and send it to the repo
                    FileDocument doc = new FileDocument(file.getAbsolutePath(), file.getName(),
                            file.lastModified(), file.length(), content);
                    repository.save(doc);
                } catch (Exception e) {}
            }
        }
    }
}